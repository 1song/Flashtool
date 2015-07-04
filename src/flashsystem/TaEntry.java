package flashsystem;

import java.io.ByteArrayInputStream;
import java.lang.Byte;

import org.util.BytesUtil;
import org.util.HexDump;

import com.igormaznitsa.jbbp.io.JBBPBitInputStream;

public class TaEntry {

	String _unit="";
	String _data="";
	String _size="";
	
	public TaEntry() {
	}
	
	public void setUnit(int unit) {
		_unit = HexDump.toHex(unit);
	}
	
	public void setUnit(String unit) {
		_unit = unit;
	}
	
	public void addData(String data) {
		_data = _data + " "+data;
		_data = _data.trim();
	}
	
	public void setData(byte[] d) {
		_data = "";
		for (int i = 0;i<d.length;i++) {
			String dataS = HexDump.toHex(d[i]);
			addData(dataS.substring(dataS.length()-2));
		}
	}
	
	public void addData(char[] data) {
		for (int i = 0;i<data.length;i++) {
			String dataS = HexDump.toHex(data[i]);
			addData(dataS.substring(dataS.length()-2));
		}
	}
	
	public String getUnit() {
		return _unit;
	}
	
	public Byte[] getUnitBytes() {
		return BytesUtil.getBytes(_unit);
	}

	public byte[] getUnitbytes() {
		Byte[] uB=BytesUtil.getBytes(_unit);
		byte[] ub = new byte[uB.length];
		for (int i=0;i<ub.length;i++) {
			ub[i]=uB[i];
		}
		return ub;
	}

	public void setSize(String size) {
		_size="0000"+size;
	}
	
	public void resize(int newsize) {
		int cursize=0;
		if (_data.length()==2) {
			cursize=1;
		}
		else {
			cursize = _data.split(" ").length;
		}
		if (newsize > cursize) {
			for (int i = cursize;i<newsize;i++) {
				addData("FF");
			}
		}
		else if (newsize<cursize) {
			_data="";
			for (int i = 0;i<newsize;i++) {
				addData("FF");
			}			
		}
	}
	
	public String getComputedSize() {
		String lsize="";
		if (_data.length()==0)
			lsize=HexDump.toHex((int)0);
		else
			if (_data.length()==2)
				lsize=HexDump.toHex((int)1);
			else
				lsize= HexDump.toHex(_data.split(" ").length);
		lsize="0000" + lsize.substring(lsize.length()-4);
		return lsize;
	}

	public int getDataSize() {
		return Integer.parseInt(getComputedSize());
	}
	
	public Byte[] getSizeBytes() {
		return BytesUtil.getBytes(getComputedSize());
	}
	
	public String getSize() {
		return _size;
	}
	
	public String getData() {
		return _data;
	}
	
	public String getDataString() {
		String[] result = _data.split(" ");
		byte[] b = new byte[result.length];
		for (int i=0;i<result.length;i++) {
			b[i]=BytesUtil.getBytes(result[i])[0];
		}
		return new String(b);
	}

	public String getDataHex() {
		String[] result = _data.split(" ");
		byte[] b = new byte[result.length];
		for (int i=0;i<result.length;i++) {
			b[i]=BytesUtil.getBytes(result[i])[0];
		}
		return HexDump.toHex(b);
	}
	
	public Byte[] getDataBytes() {
		if (_data.length()>0) {
			String[] datas = _data.split(" ");
			Byte[] data = new Byte[datas.length];
			for (int j=0;j<datas.length;j++) {
				data[j]=BytesUtil.getBytes(datas[j])[0];
			}
			return data;
		}
		else
			return null;
	}
	
	public Byte[] getWordByte() {
		if (getDataBytes()!=null)
			return BytesUtil.concatAll(getUnitBytes(), getSizeBytes(), getDataBytes());
		else
			return BytesUtil.concatAll(getUnitBytes(), getSizeBytes());
	}

	public byte[] getWordbyte() {
		Byte[] b1 = getWordByte();
		byte[] b = new byte[b1.length];
		for (int i=0;i<b1.length;i++) {
			b[i]=b1[i];
		}
		return b;
	}

	public String toString() {
		return getUnit()+" "+getComputedSize().substring(4)+" "+getData();
	}
	
	public void close() throws TaParseException {
		if (Integer.parseInt(getComputedSize(),16)!=Integer.parseInt(_size,16)) {
			throw new TaParseException("TA entry ("+getUnit()+")parsing error");
		}
	}
	
}
