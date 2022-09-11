package org.infinitypfm.bitcoin.wallet.helpers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class FloatToPlainString  extends JsonSerializer<Float> {

	public FloatToPlainString() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void serialize(Float value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

		BigDecimal d = new BigDecimal(value);
        gen.writeNumber(d.setScale(8, RoundingMode.CEILING).toPlainString());
		
	}

}
