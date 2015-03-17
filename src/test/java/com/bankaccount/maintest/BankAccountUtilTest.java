package com.bankaccount.maintest;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.bankaccount.main.BankAccountUtil;
import com.bankaccount.main.BankAccountUtil.BankAccount;

/**
 * Test cases for BankAccount Util Class
 * @author S
 *
 */
public class BankAccountUtilTest {


	@Test
	public void testExpiryDateInDescendingOrder() 
	{
		List<BankAccount> bankAccountList=new LinkedList<BankAccount>();	
		DateFormat df = new SimpleDateFormat("MMM-yyyy");
		try {
			bankAccountList.add(new BankAccountUtil.BankAccount("HSBC Canada", "5601-2345-3446-5678",df.parse("Nov-2017")));
			bankAccountList.add(new BankAccountUtil.BankAccount("Royal Bank of  Canada","4519-4532-4524-2456",df.parse("Oct-2017")));
			bankAccountList.add(new BankAccountUtil.BankAccount("American Express","3786-7334-8965-345",df.parse("Dec-2018")));
			bankAccountList.add(new BankAccountUtil.BankAccount("American Express","3786-7334-8965-348",df.parse("Mar-2019")));
			bankAccountList.add(new BankAccountUtil.BankAccount("American Express","3786-7334-8965-349",df.parse("Jan-2030")));
		   }
		catch (ParseException e)
		   {
			e.printStackTrace();
		   }
		
		BankAccountUtil obj = new BankAccountUtil();
		obj.sort(bankAccountList);
		//check expiry date in descending order
		assertEquals("Jan-2030",df.format(bankAccountList.get(0).getExpiryDate()));
		assertEquals("Mar-2019",df.format(bankAccountList.get(1).getExpiryDate()));
		assertEquals("Dec-2018",df.format(bankAccountList.get(2).getExpiryDate()));
		assertEquals("Nov-2017",df.format(bankAccountList.get(3).getExpiryDate()));
		assertEquals("Oct-2017",df.format(bankAccountList.get(4).getExpiryDate()));
			
	}
	
	@Test
	public void testCardNumberMasked()
	{
		List<BankAccount> bankAccountList=new LinkedList<BankAccount>();
		DateFormat df = new SimpleDateFormat("MMM-yyyy");
		try {
			bankAccountList.add(new BankAccountUtil.BankAccount("HSBC Canada", "5601-2345-3446-5678",df.parse("Nov-2017")));
			bankAccountList.add(new BankAccountUtil.BankAccount("Royal Bank of  Canada","4519-4532-4524-2456",df.parse("Oct-2017")));
			bankAccountList.add(new BankAccountUtil.BankAccount("American Express","3786-7334-8965-345",df.parse("Dec-2018")));
		   }
		catch (ParseException e)
		   {
			e.printStackTrace();
		   }
		
		BankAccountUtil obj = new BankAccountUtil();
		obj.maskAndSetCardNumbers(bankAccountList);
		assertEquals("56xx-xxxx-xxxx-xxxx", bankAccountList.get(0).getMaskedCardNumber());
		assertEquals("4519-xxxx-xxxx-xxxx", bankAccountList.get(1).getMaskedCardNumber());
		assertEquals("xxxx-xxxx-xxxx-345", bankAccountList.get(2).getMaskedCardNumber());
		
	}
	
}
