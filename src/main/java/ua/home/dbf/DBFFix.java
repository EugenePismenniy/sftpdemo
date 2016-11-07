package ua.home.dbf;

import org.xBaseJ.DBF;
import org.xBaseJ.fields.*;
import org.xBaseJ.xBaseJException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @author it100985pev on 07.11.16 17:04.
 */
class DBFFix extends DBF {

	private int numOfTooLongFieldName;

	public DBFFix(String dbfName) throws xBaseJException, IOException {
		super(dbfName, DBFReader.DBF_ENCODE);
		numOfTooLongFieldName = 0;
	}

	@Override
	protected Field read_Field_header() throws IOException, xBaseJException {


		byte[] byter = new byte[15];
		this.file.readFully(byter, 0, 11);

		int i;
		for (i = 0; i < 12 && byter[i] != 0; ++i) {
			;
		}

		String name;
		try {
			name = new String(byter, 0, i, encodedType);
		} catch (UnsupportedEncodingException var10) {
			name = new String(byter, 0, i);
		}

		name = name.trim();
		if (name.length() > 10) {
			String fNum = String.valueOf(numOfTooLongFieldName ++);
			name = name.substring(0, 10 - fNum.length()) + fNum;
		}

		char type = (char) this.file.readByte();
		this.file.readFully(byter, 0, 4);
		byte length = this.file.readByte();
		int iLength;
		if (length > 0) {
			iLength = length;
		} else {
			iLength = 256 + length;
		}

		byte decpoint = this.file.readByte();
		this.file.readFully(byter, 0, 14);
		Object tField;
		switch (type) {
			case 'C':
				tField = new CharField(name, iLength, this.buffer);
				break;
			case 'D':
				tField = new DateField(name, this.buffer);
				break;
			case 'E':
			case 'G':
			case 'H':
			case 'I':
			case 'J':
			case 'K':
			case 'O':
			default:
				throw new xBaseJException("Unknown Field type \'" + type + "\' for " + name);
			case 'F':
				tField = new FloatField(name, iLength, decpoint, this.buffer);
				break;
			case 'L':
				tField = new LogicalField(name, this.buffer);
				break;
			case 'M':
				tField = new MemoField(name, this.buffer, this.dbtobj);
				break;
			case 'N':
				tField = new NumField(name, iLength, decpoint, this.buffer);
				break;
			case 'P':
				tField = new PictureField(name, this.buffer, this.dbtobj);
		}

		return (Field) tField;


	}
}
