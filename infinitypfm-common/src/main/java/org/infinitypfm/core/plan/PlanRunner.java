/*
 * Copyright (c) 2005-2023 Wayne Gray All rights reserved
 * 
 * This file is part of Infinity PFM.
 * 
 * Infinity PFM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Infinity PFM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Infinity PFM.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.infinitypfm.core.plan;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.infinitypfm.core.data.DataFormatUtil;
import org.infinitypfm.core.data.NumberFormat;
import org.infinitypfm.core.data.Plan;
import org.infinitypfm.core.data.PlanEvent;
import org.infinitypfm.core.data.PlanEventType;
import org.infinitypfm.core.data.PlanRun;

public class PlanRunner {

	private DataFormatUtil _formatter;
	
	public PlanRunner() {
		_formatter = new DataFormatUtil();
	}
	
	public void run(int planId, SqlSession session) {
		
		session.delete("deletePlanRunById", planId);
		
		Plan plan = session.selectOne("getPlanById", planId);
		List<PlanEvent> planEvents = session.selectList("getPlanEventsById", planId);
		int currAge = plan.getStartAge();
		Date runDate = new Date();
		/*
		Draw BIGINT,
		DrawPercent BIGINT,
		EarnInvest BIGINT,
		EarnWage BIGINT,
		Contribution BIGINT,
		Tax BIGINT,
		 * 
		 */
		long currBalance = plan.getStartBalance();
		long currTax = 0;
		long currEarnInvest = 0;
		long currEarnWage = 0;
		long currContribution=0;
		long currDraw = 0;
		
		if (planEvents == null || planEvents.size() == 0) return;
		
		while (currAge < 96 && currBalance > 0) {
			
			PlanRun run = new PlanRun();
			
			for (PlanEvent event : planEvents) {
			
				if (event.getStartAge() > currAge || event.getEndAge() < currAge) continue;
				
				String planValue = _formatter.getAmountFormatted(event.getEventValue(),NumberFormat.getNoCommaNoParems(8));
				
				switch (event.getEventTypeId()) {
				case PlanEventType.EARN:
					currEarnWage += event.getEventValue();
					break;
				case PlanEventType.INVEST:
					currContribution += event.getEventValue();
					currBalance += event.getEventValue();
					break;
				case PlanEventType.RETURN:
					long lReturn = getPercentage(currBalance, planValue);
					currEarnInvest += lReturn;
					currBalance += lReturn;
					break;
				case PlanEventType.DRAW:
					//0=Number, 1=perceont TODO make this a type
					if (event.getEventValueType()==1) {
						long lDraw = getPercentage(currBalance, planValue);
						currDraw += lDraw;
						currBalance -= lDraw;
					} else {
						currDraw += event.getEventValue();
						currBalance -= event.getEventValue();
					}
					break;
				
				}
			}
		
			// TODO:  Calculate proper tax on earnings
			currTax = getPercentage((currDraw + currEarnWage), "28");
			run.setAge(currAge);
			run.setContribution(currContribution);
			run.setDraw(currDraw);
			run.setEarnInvest(currEarnInvest);
			run.setEarnWage(currEarnWage);
			run.setPlanID(plan.getPlanID());
			run.setRemaining(currBalance);
			run.setTax(currTax);
			run.setRunDate(runDate);
			
			session.update("insertPlanRun", run);
			
			currAge+=1;
			currEarnInvest = 0;
			currEarnWage = 0;
			currContribution=0;
			currDraw = 0;
		}
		
	}
	
	private long getPercentage(long balance, String percent) {
		BigDecimal pctReturn = _formatter.strictDivide(percent, "100",2);
		BigDecimal newBalance = _formatter.strictMultiply(pctReturn.toPlainString(), _formatter.getAmountFormatted(balance, NumberFormat.getNoCommaNoParems(8)));
		return _formatter.moneyToLong(newBalance);
	}
	
}
