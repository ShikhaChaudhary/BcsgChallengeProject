package com.bankaccount.main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class BankAccountUtil {

//	public static final String FILE_LOCATION = "/mid-test.csv";

	public static void main(String[] args) {
		
		if(args.length < 1){
			
			System.out.println("Usage error: Please pass csv location as argument value");
			System.exit(0);
		}
		
		//Read the file location from java argument
		String fileLocation = args[0];
		
		BankAccountUtil obj = new BankAccountUtil();
		List<BankAccount> accountLst = obj.getCSVFileRowsAsBankAccountList(fileLocation);

		//Sort the passed BankAccount list
		obj.sort(accountLst);
		
		//Print the sorted BankAccount List
		System.out.println("\n" +"Bank Details With Sorted Expiry Date" +"\n");
		
		//print account details
		obj.printAccoutDetails(accountLst, false);
		
		//Mask and print the masked BankAccount list
		System.out.println("\n" + "Bank Details With Masked Account Numbers"+"\n" );
		obj.maskAndSetCardNumbers(accountLst);

		//print account details with masked numbers
		obj.printAccoutDetails(accountLst, true);
		
	}


	/**
	 * Method to sort the passed BankAccount list by expiry date
	 * 
	 * @param bankAccountList
	 */
	public void sort(List<BankAccount> bankAccountList) {
		Collections.sort(bankAccountList, new OrderExpirydate());
	}


	/**
	 * Method to mask Bank Account card number with specific pattern based on bank type
	 * 
	 * @param lst
	 */
	public void maskAndSetCardNumbers(List<BankAccount> lst) {
		String maskpattern = null;
		for (BankAccount account : lst) {

			if (account.getBank().equals("American Express")) {
				maskpattern = "xxxx-xxxx-xxxx-###";
			} else if (account.getBank().equals("HSBC Canada")) {
				maskpattern = "##xx-xxxx-xxxx-xxxx";
			} else if (account.getBank().equals("Royal Bank of  Canada")) {
				maskpattern = "####-xxxx-xxxx-xxxx";
			}
			
			account.setMaskedCardNumber(maskNumber(account.getCardNumber(), maskpattern));
		}

	}

	private void printAccoutDetails(List<BankAccount> accountLst, boolean showMaskedCardNumber) {
		DateFormat df = new SimpleDateFormat("MMM-yyyy");
		for (BankAccount account: accountLst)
			
			if(!showMaskedCardNumber){
				System.out.println("Bank = " + account.getBank()
						+ " , Card Number = " + account.getCardNumber()
						+ " , Expiry date = "
						+ df.format(account.getExpiryDate()));
			}
			else{
				System.out.println("Bank = " + account.getBank()
						+ ", Card Number = " + account.getCardNumber()
						+ ", Masked card = " + account.getMaskedCardNumber());
			}
		

	}

	public List<BankAccount> getCSVFileRowsAsBankAccountList(String fileLocation) {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		List<BankAccount> list = new ArrayList<BankAccount>();
		DateFormat df = new SimpleDateFormat("MMM-yyyy");
		// Date startDate = df.parse(startDate);

		File csvFile = new File(fileLocation);
		if(!csvFile.exists())
			throw new IllegalArgumentException("File does not exists at given location["+ fileLocation + "]");
		
		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] bankdetailsArray = line.split(cvsSplitBy);
				BankAccount bankdetails = new BankAccount();
				bankdetails.setBank(bankdetailsArray[0]);
				bankdetails.setCardNumber(bankdetailsArray[1]);
				try {
					bankdetails.setExpiryDate(df.parse(bankdetailsArray[2]));
				} catch (ParseException e) {
					
					e.printStackTrace();
				}
				list.add(bankdetails);
				

			}

			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		
		return list;
	}

	private String maskNumber(String number, String mask) {

		int index = 0;
		StringBuilder masked = new StringBuilder();
		for (int i = 0; i < mask.length(); i++) {
			char c = mask.charAt(i);
			if (c == '#') {
				masked.append(number.charAt(index));
				index++;
			} else if (c == 'x') {
				masked.append(c);
				index++;
			} else {
				masked.append(c);
				index++;
			}
			
		}
		return masked.toString();
	}

	/**
	 * BankAccount class holding bank account card details
	 * 
	 * @author S
	 *
	 */
	public static class BankAccount {
		private String bank;
		private String cardNumber;
		private Date expiryDate;
		private String maskedCardNumber;
		
		

		
		public BankAccount() {
			super();
		}

		public BankAccount(String bank, String cardNumber, Date expiryDate) {
			super();
			this.bank = bank;
			this.cardNumber = cardNumber;
			this.expiryDate = expiryDate;
		}

		public String getBank() {
			return bank;
		}

		public void setBank(String bank) {
			this.bank = bank;
		}

		public String getCardNumber() {
			return cardNumber;
		}

		public void setCardNumber(String cardNumber) {
			this.cardNumber = cardNumber;
		}

		public Date getExpiryDate() {
			return expiryDate;
		}

		public void setExpiryDate(Date expiryDate) {
			this.expiryDate = expiryDate;
		}
		
		public void setMaskedCardNumber(String MaskedCardNumber)
		{
			this.maskedCardNumber=MaskedCardNumber;
		}
		
		public String getMaskedCardNumber()
		{
			return maskedCardNumber;
		}

	}

	/**
	 * Comparator to sort the BankAccount objects based on expiry date
	 * @author S
	 *
	 */
	public static class OrderExpirydate implements Comparator<BankAccount> {

		@Override
		public int compare(BankAccount o1, BankAccount o2) {
			return o1.expiryDate.after(o2.expiryDate) ? -1 : (o1.expiryDate
					.before(o2.expiryDate) ? 1 : 0);
		}
	}

}
