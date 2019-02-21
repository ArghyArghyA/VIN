import java.util.HashMap;

import vin.VIN;

public class Test {
	private static int[] vinDigitPositionMultiplier = { 8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2 };
	private static HashMap<String, Integer> vinDigitValues = new HashMap<String, Integer>();

	static {
		vinDigitValues.put("A", 1);
		vinDigitValues.put("B", 2);
		vinDigitValues.put("C", 3);
		vinDigitValues.put("D", 4);
		vinDigitValues.put("E", 5);
		vinDigitValues.put("F", 6);
		vinDigitValues.put("G", 7);
		vinDigitValues.put("H", 8);
		vinDigitValues.put("J", 1);
		vinDigitValues.put("K", 2);
		vinDigitValues.put("L", 3);
		vinDigitValues.put("M", 4);
		vinDigitValues.put("N", 5);
		vinDigitValues.put("P", 7);
		vinDigitValues.put("R", 9);
		vinDigitValues.put("S", 2);
		vinDigitValues.put("T", 3);
		vinDigitValues.put("U", 4);
		vinDigitValues.put("V", 5);
		vinDigitValues.put("W", 6);
		vinDigitValues.put("X", 7);
		vinDigitValues.put("Y", 8);
		vinDigitValues.put("Z", 9);
		vinDigitValues.put("1", 1);
		vinDigitValues.put("2", 2);
		vinDigitValues.put("3", 3);
		vinDigitValues.put("4", 4);
		vinDigitValues.put("5", 5);
		vinDigitValues.put("6", 6);
		vinDigitValues.put("7", 7);
		vinDigitValues.put("8", 8);
		vinDigitValues.put("9", 9);
		vinDigitValues.put("0", 0);
	}
	
	public static String getCheckSumChar(String vin) {
		int checkSumTotal = 0;

		if (vin.length()!= 17)
			return null;
		for (int i = 0; i < vin.length(); i++) {
			if (vinDigitValues.get(Character.toString(vin.charAt(i))) != null)
				checkSumTotal = checkSumTotal + vinDigitValues.get(Character.toString(vin.charAt(i))) * vinDigitPositionMultiplier[i];
			else
				return null;
		}
		return (checkSumTotal%11==10)? "X":Integer.toString(checkSumTotal%11);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for (int i = 0; i<1000; i++)
		{
			String V = VIN.getRandomVin();
			System.out.println(V);
//			String V = "1FACP45EXLF192944";
//			String X = V.substring(8, 9);
//			String Y = getCheckSumChar(V);
//			System.out.println(X.equals(Y));
		}
//		System.out.println(VIN.getCheckSumChar("1GCJK34GDLX5VF40"));
//		System.out.println(VIN.getCheckSumChar("1FACP45EXLF192944"));

	}

}
