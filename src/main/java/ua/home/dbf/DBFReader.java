package ua.home.dbf;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.xBaseJ.DBF;
import org.xBaseJ.fields.*;
import org.xBaseJ.xBaseJException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Hello DBF!
 *
 */
public class DBFReader
{

	public static final String DBF_ENCODE = "cp866";

	public static void dbfProcessing(String dbfName) throws IOException, xBaseJException {


		DBF dbf = new DBFFix(dbfName);




		try {
			final int recordCount = dbf.getRecordCount();
			final int fieldCount = dbf.getFieldCount();

			System.out.printf("Record Count = %s; Field Count = %s\n", recordCount, fieldCount);

			for (int fieldIndex = 1; fieldIndex <= fieldCount; fieldIndex ++) {

				Field field = dbf.getField(fieldIndex);

				String fieldName = field.Name;
				char fieldType = field.getType();
				int length = field.Length;
				String simpleFieldClassName = field.getClass().getSimpleName();

				int decimalPositionCount = field.getDecimalPositionCount();

				System.out.printf("Field Name = '%s'; Type = '%s'; Length = %s; Simple Field Class Name = '%s'; Decimal Position Count = %s\n"
						, fieldName, fieldType, length, simpleFieldClassName, decimalPositionCount);
			}


			// ---------------------------------------------------

			Object[] values = new Object[fieldCount];

			for (int record = 1; record <= recordCount; record ++) {

				dbf.read();

				for (int fieldIndex = 1, i = 0; fieldIndex <= fieldCount; fieldIndex ++, i ++) {

					Field field = dbf.getField(fieldIndex);
					char type = field.getType();

					String value = StringUtils.trimToNull(field.get());

					if (value == null) {
						values[i] = null;
					} else {

						switch (type) {

							case 'D': {
								DateField dateField = (DateField) field;
								Calendar calendar = dateField.getCalendar();
								values[i] = calendar.getTime();
							}
							break;


							case 'N':
							case 'F':  {
								if (!".".equals(value)) {
									BigDecimal bigDecimal = new BigDecimal(value);

									if (field.getDecimalPositionCount() == 0) {
										values[i] = bigDecimal.toBigInteger();
									} else {
										values[i] = bigDecimal;
									}

									//System.out.printf("%s;  len = %s; dec = %s; origin value = '%s'\n", values[i], field.Length, field.getDecimalPositionCount(), value);
								} else {
									values[i] = null;
								}
							}
							break;

							case 'L': {
								LogicalField logicalField = (LogicalField) field;
								values[i] = logicalField.getBoolean();
							}  break;


							default: {
								values[i] = value;
							}

						}
					}
				} // for


				System.out.println(Arrays.toString(values));

				//			System.out.printf(recordLineFormat, values);
			} // for
		} finally {
			closeDbf(dbf);
		}
	}

	public static void closeDbf(DBF dbf) {
		if (dbf != null) {
			try {
				dbf.close();
			} catch (Exception e) {
				System.out.printf("Error close DBF: %s, %s\n",
						ExceptionUtils.getMessage(e), ExceptionUtils.getMessage(e.getCause()));
			}
		}
	}

}
