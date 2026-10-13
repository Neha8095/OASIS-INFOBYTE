public class Account {
	private String accountNumber;
	private String customerName;
	private String pin;
	private String mobileNumber;
	private String accountType;
	private double balance;

	private final String[] transactionTypes;
	private final double[] transactionAmounts;
	private int transactionCount;

	public Account() {
		this("", "", "", "", "", 0.0);
	}

	public Account(String accountNumber, String customerName, String pin,
				   String mobileNumber, String accountType, double balance) {
		this.accountNumber = accountNumber;
		this.customerName = customerName;
		this.pin = pin;
		this.mobileNumber = mobileNumber;
		this.accountType = accountType;
		this.balance = balance;
		this.transactionTypes = new String[10];
		this.transactionAmounts = new double[10];
		this.transactionCount = 0;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public void addTransaction(String type, double amount) {
		if (transactionCount == transactionTypes.length) {
			for (int index = 1; index < transactionCount; index++) {
				transactionTypes[index - 1] = transactionTypes[index];
				transactionAmounts[index - 1] = transactionAmounts[index];
			}
			transactionCount--;
		}

		transactionTypes[transactionCount] = type;
		transactionAmounts[transactionCount] = amount;
		transactionCount++;
	}

	public int getTransactionCount() {
		return transactionCount;
	}

	public String getTransactionType(int index) {
		return transactionTypes[index];
	}

	public double getTransactionAmount(int index) {
		return transactionAmounts[index];
	}
}
