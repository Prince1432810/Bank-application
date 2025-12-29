import java.util.*;
import java.io.*;

public class CityBank{
    static String[] user = new String[100];
    static int userCount = 0;
    static int ext = 2001;
    static Scanner sc = new Scanner(System.in);

    public static void main(String args[]) throws Exception {
        while(true){
            ATM.printLine('_', 82);
        
            System.out.println();
            
            Scanner sc = new Scanner(System.in);
                
                ATM[] holder = new ATM[user.length];
                String str;
                int check;
                int accountNumber;
                
                for(int i=0; i<user.length; i++)
                    holder[i] = new ATM();
                
                str="Are you a new User ? (0 == no, 1 == yes, (exit == -1)): ";
                check = ATM.handleInput(str, -1, 1);
                
                if(-1 == check)
                    return;
                
                if(check == 1)
                    openAccount();
                
                while(true){
                    accountNumber = -1;

                    
                    System.out.print("Enter your account number (exit -1): ");
                    accountNumber = sc.nextInt();
                    
                    
                    if(accountNumber == -1)
                        break;
                    
                    accountNumber -= ext;

                    if(user[accountNumber] == null){
                        System.out.println("\nSorry!, You don't have any account");
                        System.out.println("Create an account to get account number");
                        ATM.printLine('*', 20);
                        System.out.println();
                        
                        str = "Want to create an account ? (0 == no, 1 == yes): ";
                        check = ATM.handleInput(str, 0, 1);
                        
                        if(check == 1)
                            openAccount();
                        else
                            continue;
                    }


                    if(accountNumber >= user.length) {
                        
                        ATM.printLine('-', 23);
                        System.out.print("\n| Invalid account number. Please try again  |");
                        ATM.printLine('-', 23);
                        System.out.println();
                        
                        continue;
                    }
                    
                    holder[accountNumber].validateAccount(accountNumber, user[accountNumber]);

                    ATM.displayServices(holder[accountNumber], accountNumber, user[accountNumber]);          
                    
                    for(int i=0; i<50; i++)
                        System.out.println();

                    displayMenu();
                }
        }
    }

    public static void openAccount(){
        System.out.print("Enter your name : ");
        user[userCount] = sc.nextLine();
        
        System.out.println("Your Account number is = "+(userCount+ext));
        ATM.printLine('*', 20);
        System.out.println();
        
        System.out.println("You have successfuly created an account in City Bank");
        System.out.println("Login to proceed");
        userCount++;
    }

    public static void displayMenu(){
        System.out.println("List of Account Holders and Their Account Numbers");
        
        for(int i=0; i<userCount; i++){
            System.out.println((i+1)+". "+(i+ext)+" : "+user[i]);
        }
        
        System.out.println("------------------------------------------------------");
    }
    public static void toShow(){
        System.out.println("List of Account Holders and Their Account Numbers (who's last digit multiple of : 4)");

        for(int i=0; i<userCount; i++){
            if((i+ext)%4 == 0)
                System.out.println((i+1)+". "+(i+ext)+" : "+user[i]);
        }
    }

}

class ATM {
    static String str;
    static Date date = new Date();
    static File[] account = new File[CityBank.user.length];
    static Scanner sc = new Scanner(System.in);
    int passKey;
    static double holderAmount=0;
    boolean isActive = false;
    static String loans[] = {"Personal", "Home", "Vehicle", "Education", "Business", "Gold"};
    static int interstRate[] = {10, 7, 8, 9, 11, 7};

    public static void displayServices(ATM holder, int accountNumber, String user) throws Exception{
        holder.openAccount(accountNumber, user);
        int choice;
        
        services();
        
        while(true){
            System.out.println("------------------------------------------------------");
            System.out.print("\nEnter your choice (exit -1) : ");
            choice = sc.nextInt();
            
            if(choice == -1)
            break;
            switch(choice){

                case 0:
                services();
                break;

                case 1:
                holder.handleDeposit(accountNumber, user);
                break;
                
                case 2:
                holder.handleWithdrawl(accountNumber, user);
                break;
                
                case 3:
                System.out.println(user+"'s Bank Balance = "+String.format("%.3f", holder.checkBalance()));
                break;
                
                case 4:
                holder.handlePin(accountNumber, user);
                break;
                
                case 5:
                holder.trsHistory(accountNumber);
                break;
                
                case 6:
                holder.loanOptions(accountNumber, user);
                break;

                case 7:
                holder.annualInterest(accountNumber);
                break;

                case 8:
                CityBank.toShow();
                break;

                default:
                ATM.invalidInput();
                System.out.println();
                break;
            }
        }
    }

    

    public  void annualInterest(int accountNumber){
        double amount = 0;
        amount = (holderAmount * 2.3 * 1) / 100;
        System.out.println("Annual Interest on current balance ("+String.format("%.3f", holderAmount)+") is : "+String.format("%.3f", amount));
    }

    public boolean check(){
        return isActive;
    }
    
    public double checkBalance(){
        return holderAmount;
    }
    
    public static void printLine(char c, int count) {
        System.out.println();
        for (int i = 0; i < count; i++) {
            System.out.print(c + " ");
        }
    }
    
    public static void invalidInput(){
        printLine('-', 9);
        System.out.print("\n| Invalid Input |");
        printLine('-', 9);
        System.out.println();
    }
    
    public boolean validateHolder(int pin){
        if(passKey == pin){
            printLine('+', 10);
            System.out.print("\n+ Access Granted  +");
            printLine('+', 10);
            System.out.println("\n");
            return true;
        }
        else
        return false;
    }
    
    public String changePin(int pin, String name){
        name = date.toString()+"       Pin Changed by "+name+"\n";
        
        passKey = pin;
        createPin();
        
        return name;
    }
    
    public void createPin(){
        isActive = true;
        System.out.print("Please set your Pin : ");
        passKey = sc.nextInt();
        
        printLine('+', 15);
        System.out.print("\n+ Pin Created Successfully  +");
        printLine('+', 15);
        
        System.out.println("\n");
        sc.nextLine();
    }
    
    public String deposit() {
        double amount = validateAmount("deposit", 0); // No balance check for deposits
        holderAmount += amount;
        
        sc.nextLine();
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        
        System.out.println("Amount (" + amount + ") deposited successfully by " + name + ".");
        return date.toString() + "       Amount (" + amount + ") deposited by " + name + "\n";
    }
    
    public void loanOptions(int accountNumber, String accountHolder) throws Exception{
        
        printLine('-', 30);
        System.out.println();
        
        for(int i=0; i<loans.length; i++){
            System.out.println(i+1+". "+loans[i]+" Loan : "+interstRate[i]+"% p.a.");
        }

        printLine('-', 30);
        System.out.println();

        int choice=0;

        str = "\nEnter your choice : ";
        choice = handleInput(str,1, 6)-1;

        str = "\nEnter amount : ";
        double amount = handleInput(str,1, Integer.MAX_VALUE);

        str = "\nEnter time in year (max - 10): ";
        int year = handleInput(str, 1, 10);

        printLine('-', 30);

        getLoan(accountNumber, accountHolder, choice, amount, year);
    }

    public static int handleInput(String str, int start, int end){
        int item;
       
        while(true){
            
            System.out.print(str);
            item = sc.nextInt();
            
            if(item >= start && item <= end)
                return item;
            else
                invalidInput();
        }
    }

    public static void getLoan(int accountNumber, String accountHolder, int loanType, double amount, int year) throws Exception{
        System.out.println();

        ATM.printLine('+', 15);
        System.out.print("\n| "+loans[loanType]+" Loan Approved");
        ATM.printLine('+', 15);
        System.out.println();

        claculateEMI(amount, interstRate[loanType], year, loans[loanType]);
        addTransaction(loanDeposit(accountNumber, accountHolder, amount, loanType), accountNumber, accountHolder);
    }

    public static void claculateEMI(double amount, int interstRate, int time, String loanName){
        double mAmt = ((amount/time)/12)*interstRate/100;
        double monthlyInstallment = mAmt+((amount/time)/12);
        
        System.out.println("\nCongratulation for "+loanName+" Loan.");
        System.out.println("According to the interest rate ("+interstRate+"%) of "+loanName+" Loan");
        
        System.out.println("\nYour monthly installment = "+String.format("%.3f", monthlyInstallment));
        System.out.println("Your yearly installment = "+String.format("%.3f", monthlyInstallment*12));
        System.out.println("Total Amount to be paid to Bank = "+String.format("%.3f", monthlyInstallment*12*time));
    }

    public static String loanDeposit(int acountNumber, String accountHolder, double amount, int loanType){
        holderAmount += amount;
        
        System.out.println("\nAmount (" + amount + ") credited successfully of "+ loans[loanType] +" Loan.");
        return date.toString() + "       Amount (" + amount + ") credited of "+ loans[loanType] +" Loan\n";
    }
    
    public void validateAccount(int accountNumber, String user){
        int pin;
        
        if(!check()){
            System.out.println("------------------------------------------------------");
            System.out.println("Hello! "+user+", Thanks for joining our Bank");
            System.out.println("You have to create a Security Pin to use our services.\n");
            createPin();
        }
        else{
            System.out.println("------------------------------------------------------");
            System.out.print("Enter your pin : ");
            
            pin = sc.nextInt();
            int pinCount=0;
            
            while (!validateHolder(pin)) {
                
                if(pinCount == 4)
                break;

                printLine('-', 9);
                System.out.print("\n| Incorrect Pin |");
                printLine('-', 9);
                
                System.out.println("\n");
                
                if(pinCount != 0)
                    System.out.println((4-pinCount)+"Attempts left");
                
                System.out.print("Enter your pin : ");
                pin = sc.nextInt();
                pinCount++;
            }
        }
    }

    public void trsHistory(int accountNumber) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(account[accountNumber]));
        String line;
        line = br.readLine();
        
        System.out.println("Transaction History of the Passbook : \n");
        
        if(line == null){
            System.out.println("Make Transactions to see the Transaction History");
            br.close();
            return;
        }
        do{
            System.out.println(line);
        }while((line = br.readLine()) != null);
        
        br.close();
    }
    
    public void openAccount(int accountNumber, String user) throws IOException{
        account[accountNumber] = new File(user+".txt");
        account[accountNumber].createNewFile();
    }
    
    public static void addTransaction(String transaction, int accountNumber, String user) throws Exception{
        
        if(transaction == null)
        return;
        
        FileWriter fileWriter = new FileWriter(account[accountNumber].getName(), true);
        fileWriter.write(transaction);
        fileWriter.close();
    }
    
    public static double validateAmount(String action, double currentBalance) {
        double amount;
        
        while (true) {
            System.out.print("Enter the amount to " + action + ": ");
            amount = sc.nextInt();
            
            if (amount <= 0) {
                System.out.println("Invalid input. Amount must be greater than zero.");
            } else if (action.equalsIgnoreCase("withdraw") && amount > currentBalance) {
                System.out.println("Insufficient funds. Please enter a smaller amount.");
            } else {
                break;
            }
        }
        
        return amount;
    }
    
    
    public String withdraw() {
        double amount= validateAmount("withdraw", holderAmount); // Ensure amount doesn't exceed current balance
        holderAmount -= amount;
        
        sc.nextLine();
        System.out.print("Enter your name: ");
        String name = sc.nextLine();
        
        System.out.println("Amount (" + amount + ") withdrawn successfully by " + name + ".");
        return date.toString() + "       Amount (" + amount + ") withdrawn by " + name + "\n";
    }
    
    
    public void handleDeposit(int accountNumber, String user) throws Exception{
        addTransaction(deposit(), accountNumber, user);
    }
    
    public void handleWithdrawl(int accountNumber, String user) throws Exception{
        
        if(checkBalance() == 0){
            printLine('-', 30);
            System.out.print("\n| Empty Account, Please deposit funds before withdrawing. |");
            printLine('-', 30);
            System.out.println();
        }
        else{
            addTransaction(withdraw(), accountNumber, user);
        }    
    }
    
    public void handlePin(int accountNumber, String user) throws Exception{
        int holderPin;
        String name;
        sc.nextLine();
        
        System.out.print("Enter your name : ");
        name = sc.nextLine();
        
        System.out.print("Enter your old pin : ");
        holderPin = sc.nextInt();
        if(!validateHolder(holderPin)){
            printLine('+', 9);
            System.out.print("\n+ Access denied +");
            printLine('+', 9);
            System.out.println("\n");
        }
        else{
            addTransaction(changePin(holderPin, name), accountNumber, user);
        }
    }
    
    public static void services(){
        System.out.println("------------------------------------------------------");
        System.out.println("These are the Services Provided by the ATM Machine");
        System.out.println();
        
        System.out.println("0. Display Services");
        System.out.println("1. Deposit Cash");
        System.out.println("2. Withdraw Cash");
        System.out.println("3. Check Balance");
        System.out.println("4. Change Pin");
        System.out.println("5. Transaction History");
        System.out.println("6. Get Loan");
        System.out.println("7. Annual Interest");
        System.out.println("8. To Show (1 accounts)");
    }
}    