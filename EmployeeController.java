import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.Scanner;

import com.ideas2it.model.Employee;
import com.ideas2it.model.EmployeeProject;
import com.ideas2it.model.LeaveRecord;
import com.ideas2it.service.EmployeeService;
import com.ideas2it.service.EmployeeProjectService;
import com.ideas2it.service.LeaveRecordService;
import com.ideas2it.service.EmployeeServiceImpl;
import com.ideas2it.service.EmployeeProjectServiceImpl;
import com.ideas2it.service.LeaveRecordServiceImpl;

import com.ideas2it.exception.EmployeeNotFoundException;
import com.ideas2it.validationutils.ValidationUtils;
import com.ideas2it.datetimeutils.DateTimeUtils;

import com.ideas2it.enums.EmployeeType;
import com.ideas2it.enums.Gender;
import com.ideas2it.enums.LeaveType;

/**
 * This class is the controller class which contains main method
 * This class gets input from the user and print the employee details
 * leave record and project details
 *
 * @author Nithish K
 * @verison 1.0
 * @since 17.09.2022
 */
public class EmployeeController  {

    private Scanner scanner = new Scanner(System.in);

    private EmployeeService employeeServiceImpl = new EmployeeServiceImpl();
    private LeaveRecordService leaveRecordServiceImpl = new LeaveRecordServiceImpl(); 
    private EmployeeProjectService employeeProjectServiceImpl = new EmployeeProjectServiceImpl();

    public static void main (String[] args) {

        EmployeeController employeeController = new EmployeeController();
        employeeController.chooseOperation();
    }

    public void chooseOperation() {
        final String EMPLOYEE_DETAILS = "1";
        final String LEAVE_RECORDS = "2";
        final String PROJECT_DETAILS = "3";
        final String EXIT_MENU = "4";
        String choice;

        do {
            System.out.println("----Enter the choice----\n"
                              +"1. Employee Details\n"
                              +"2. Leave Record\n"
                              +"3. Project Details\n"
                              +"4. Exit");
            choice = scanner.next();
        
            switch(choice) {
                case EMPLOYEE_DETAILS:
                    chooseOperationForEmployee();
                    break;

                case LEAVE_RECORDS:
                    chooseOperationForLeaveRecord();
                    break;            

                case PROJECT_DETAILS: 
                    chooseOperationForEmployeeProject();
                    break;
                
                case EXIT_MENU:
                     break;

                default:
                    System.out.println("Invalid input");
            } 
        }while(!choice.equals(EXIT_MENU));
    }    

    /**
     * Select the operation for employee like add, display, edit and remove
     * Calls another method based on the user input
     *
     * @param userChoice
     */
    public void chooseOperationForEmployee() {
        final String ADD_EMPLOYEE = "1";
        final String DISPLAY_EMPLOYEE_DETAIL = "2";
        final String EDIT_EMPLOYEE = "3";
        final String DELETE_EMPLOYEE = "4";   
        final String EXIT_EMPLOYEE = "5";
        String choice;

        do {
            choice = getUserChoice();
        
            switch(choice) {
                case ADD_EMPLOYEE:
                    addEmployee();
                    break;

                case DISPLAY_EMPLOYEE_DETAIL:
                    printEmployeeDetails();
                    break;            

                case EDIT_EMPLOYEE: 
                    editEmployeeDetails();
                    break;
     
                case DELETE_EMPLOYEE: 
                    deleteEmployee();
                    break;
                
                case EXIT_EMPLOYEE:
                     break;

                default:
                    System.out.println("Invalid input");
            } 
        }while(!choice.equals(EXIT_EMPLOYEE));
    }

    /**
     * Print the choice for create, read, update and display operation for employee 
     * 
     * @return choice this returns the choice for employee operation
     */
    public String getUserChoice() {
        StringBuilder printUserMenu = new StringBuilder();
        System.out.println(printUserMenu.append("-----Enter the choice-----\n"
                             +"1. Add an Employee\n" 
                             +"2. Display Employee details\n"
                             +"3. Edit employee details\n"
                             +"4. Delete a employee\n"
                             +"5. Exit\n"));
        String choice = scanner.next();
        return choice;
    }

    /**
     * Gets the employee details from the user.
     * This method invokes another method for creating employee id
     * Gets input for employee name, employee Type, dateOfBirth, mobile number
     * emailid, designation
     *
     * Every parameter is validating by validation util class
     */
    public void addEmployee() {
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        String employeeId = employeeServiceImpl.createEmployeeId();

        System.out.println("Enter Employee Name");
        String name = validateName();

        System.out.println("Enter the employee type\n1.Trainer\n2.Trainee");
        String choice = scanner.next();
        String employeeType = EmployeeType.getEmployeeType(choice).toString(); 

        System.out.println("Enter the employee gender\n1. Male\n2. Female");
        choice = scanner.next();
        String employeeGender = Gender.getEmployeeGender(choice).toString();

        System.out.println("Enter the date of birth in dd-mm-yyyy format");
        String dateOfBirth = validateDateOfBirth();

        System.out.println("Enter the mobile number");
        String mobileNumber = validateEmployeeMobileNumber();
 
        System.out.println("Enter the Email ID");
        String emailId = validateEmployeeEmail(); 

        System.out.println("Enter the designation");
        String designation = validateDesignation();

        String createdAt = dateTimeUtils.getDate();
        String modifiedAt = dateTimeUtils.getDate();

        Employee employee = new Employee(employeeId, employeeType, 
                                         name, dateOfBirth, employeeGender,
                                         mobileNumber, emailId, designation, 
                                         createdAt, modifiedAt);
        employeeId = employeeServiceImpl.addEmployee(employee); 
        if (employeeId != null) {
            System.out.println("\n\n---Employee created with ID: "+employeeId+" ---\n\n");
        } else {
            System.out.println("\n\n---Employee not created---\n\n");
        }
    }      

    /**
     * Get the choice for printing employees
     * and calls the required methods to print employee details 
     */
    public void printEmployeeDetails() {
        final String PRINT_EMPLOYEES = "1";
        final String PRINT_EMPLOYEE = "2";
        System.out.println("Enter the choice");
        while(true) {
            System.out.println("1. Print all employee details\n"
                               +"2. Print one employee detail");
            String choice = scanner.next();
            if(choice.equals(PRINT_EMPLOYEES)) {
                printEmployees();
                break;
            } else if (choice.equals(PRINT_EMPLOYEE)) {
                getEmployeeById();
                break;
            } else {
                System.out.println("Enter the valid option");
            }
        }
    }    

    /**
     * This method print the employee details
     */ 
    public void printEmployees() {
        StringBuilder printEmployees = new StringBuilder();
        System.out.println(printEmployees.append("-----------------------------\n"
                              +"\n----------------Employee Details---------------\n\n"
                              +employeeServiceImpl.getEmployees()
                              +"\n------------------------------"));
    }

    /**
     * Get the employee id from the user 
     */
    public void getEmployeeById() {
        try{
            System.out.println("Enter the employee ID");
            String employeeId = scanner.next();
            StringBuilder printEmployees = new StringBuilder();
            System.out.println(printEmployees.append("--------------------------------\n"
                              +"\n----------------Employee Details---------------\n\n"
                              +employeeServiceImpl.getEmployeeById(employeeId)
                              +"\n----------------------------------"));
        } catch (EmployeeNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Print the updating options for which field
     * employee want to update and get the input for 
     * which field employee want to update
     * Every parameter is validating by validation util class
     *   
     * @return choice 
     */
    public String showEmployeeEditOption() {
        String choice;
        StringBuilder printEditMenu = new StringBuilder();
        System.out.println(printEditMenu.append("Enter the option to change\n"
                           +"1. Change Employee Name\n"
                           +"2. Change Employee Type\n"
                           +"3. Change Employee Date of Birth\n"
                           +"4. Change Mobile number\n"
                           +"5. Change Email\n"
                           +"6. Change Current Project\n"
                           +"7. Exit"));
        choice = scanner.next();
        return choice;
    }

    /**
     * This method edit the employee details
     * 
     * Get input from the user for updating employee name, dateOfBirth, mobile Number 
     * emailId, current project
     * Every parameter is validating by validation util class
     */
    public void editEmployeeDetails() {
        final String CHANGE_EMPLOYEE_NAME = "1";
        final String CHANGE_EMPLOYEE_TYPE = "2";
        final String CHANGE_DATE_OF_BIRTH = "3";
        final String CHANGE_MOBILE_NUMBER = "4";
        final String CHANGE_EMAIL_ID = "5";
        final String CHANGE_DESIGNATION = "6";
        final String EXIT_EMPLOYEE_EDIT = "7";
        String choice = " ";
        Employee employee = null;
        System.out.println("Enter the employee Id");
        String employeeId = scanner.next();

        try {
	    employee = employeeServiceImpl.getEmployeeById(employeeId);
        } catch (EmployeeNotFoundException e) {
            System.out.println(e.getMessage());
        }

        if (employee != null) {
            do {
                choice = showEmployeeEditOption();                              
                switch(choice) {

                    case CHANGE_EMPLOYEE_NAME:     
                        System.out.println("Enter the new name");
                        String userChange = validateName();
                        employee.setEmployeeName(userChange);
                        updateEmployee(employee);
                        break;

                    case CHANGE_EMPLOYEE_TYPE:
                        System.out.println("Enter the employee type\n1.Trainer\n2.Trainee");
                        choice = scanner.next();
                        userChange = EmployeeType.getEmployeeType(choice).toString();
                        employee.setEmployeeType(userChange);
		        updateEmployee(employee);
                        break;
                               
                    case CHANGE_DATE_OF_BIRTH:
                        System.out.println("Enter the date of Birth"); 
                        userChange = validateDateOfBirth();
                        employee.setDateOfBirth(userChange);
		        updateEmployee(employee);
                        break;

                    case CHANGE_MOBILE_NUMBER:
                        System.out.println("Enter the new mobile number");
                        userChange = validateEmployeeMobileNumber();
                        employee.setMobileNumber(userChange);
		        updateEmployee(employee);
                        break;

                    case CHANGE_EMAIL_ID:
                        System.out.println("Enter the new email ID");
                        userChange = validateEmployeeEmail();
                        employee.setEmailId(userChange);
		        updateEmployee(employee);
                        break;
                                 
                    case CHANGE_DESIGNATION:
                        System.out.println("Enter the DESIGNATION");
                        userChange = validateDesignation();
                        employee.setDesignation(userChange);
		        updateEmployee(employee);
                        break;
    
                    case EXIT_EMPLOYEE_EDIT:
                        break;

                    default:
                        System.out.println("Invalid Option");
                        break;
                }    
            }while(!choice.equals(EXIT_EMPLOYEE_EDIT));   
        } else {
            System.out.println("----Employee Not Found----");
        }
    }

    /**
     * Request the update employee method in employee service 
     * to update the employee
     *
     * @param employee
     */
    public void updateEmployee(Employee employee) {
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        employee.setModifiedAt(dateTimeUtils.getDate());
        if(employeeServiceImpl.updateEmployee(employee)) {
            System.out.println("---Employee Updated Sucessfully---");   
        } else {
            System.out.println("---Employee not Updated---");
        }
    }
   
    /**
     * Gets the employee id which the user 
     * wants to remove from the data
     */
    public void deleteEmployee() {
        System.out.println("Enter the employee Id");
        String employeeId = scanner.next();
        int deleteEmployee = employeeServiceImpl.removeEmployee(employeeId);
        if (deleteEmployee > 0) {
            System.out.println("\n\n-----Employee Deleted Successfully------\n\n");
            leaveRecordServiceImpl.removeLeaveRecord(employeeId); 
        } else {
            System.out.println("\n\n-----Employee not found-----\n\n");
        }        
    }
    
    /**
     * Gets the input for validating 
     * the employee name
     *
     * The name will be passed to validation
     * utils class for validating
     *
     * @return name
     */
    public String validateName() {
        ValidationUtils validationUtils = new ValidationUtils();
        String name = " ";
        boolean check;
        do {
            scanner.nextLine();
            name = scanner.nextLine();
            check = validationUtils.validateName(name);
            if (check == false) {
                System.out.println("Please enter the valid name");
            }
        }while(check==false);
        return name;
    }

    /**
     * Gets the input for validating 
     * the employee mobile number
     *
     * The mobile number will be passed to validation
     * utils class for validating
     *
     * @return mobileNumber
     */
    public String validateEmployeeMobileNumber() {
        ValidationUtils validationUtils = new ValidationUtils();
        String mobileNumber = " ";
        boolean check;
        do {
            mobileNumber = scanner.next();
            check = validationUtils.validateMobile(mobileNumber);
            if (check == false) {
                System.out.println("Please enter the valid mobile number");
            }
        }while(check==false);
        return mobileNumber;
    }

    /**
     * Gets the input for validating 
     * the employee email id
     *
     * The email id will be passed to validation
     * utils class for validating
     *
     * @return emailId
     */
    public String validateEmployeeEmail() {
        ValidationUtils validationUtils = new ValidationUtils();
        String emailId = " ";
        boolean check;
        do {
            emailId = scanner.next();
            check = validationUtils.validateEmail(emailId);
            if (check == false) {
                System.out.println("Please enter the valid Email ID");
            }
        }while(check==false);
        return emailId;
    }

    /**
     * Gets the input for validating 
     * the employee Designation
     *
     * The number of projects will be passed to validation
     * utils class for validating
     *
     * @return currentProject
     */
    public String validateDesignation() {
        ValidationUtils validationUtils = new ValidationUtils();
        String designation = " ";
        boolean check;
        do {
            designation = scanner.next();
            check = validationUtils.validateDesignation(designation);
            if (check == false) {
                System.out.println("Please enter the valid Project");
            }
        }while(check==false);
        return designation;
    }

    /**
     * Get the date of birth as input and call
     * validation util class for validation
     * date of birth
     */ 
    public String validateDateOfBirth() {
        ValidationUtils validationUtils = new ValidationUtils();
        String dateOfBirth;
        boolean check = false;
        do {
            dateOfBirth = scanner.next();
            check = validationUtils.validateDateOfBirth(dateOfBirth);
            if (check == false){
                System.out.println("Please enter the valid date of birth");
            }
        }while(check==false);
        return dateOfBirth;
    }

    /**
     * Check whether the employee
     * is exist or not exist
     *
     * EmployeeNotFoundException- a custom exception is used
     * This exception occurs if employee not found
     */
    public Employee checkEmployeeId(String employeeId) {
        Employee employee = null;
        try {
             employee = employeeServiceImpl.getEmployeeById(employeeId);
        } catch (EmployeeNotFoundException e) {
             System.out.println(e);
        }
        return employee;
    }


/*------------------------LEAVE RECORDS*----------------------------*/
    
    /**
     * prints the choice for managing leave records
     * and invokes other methods based on choice
     */
    public void chooseOperationForLeaveRecord() {
        final String ADD_LEAVE_RECORD = "1";
        final String DISPLAY_LEAVE_RECORD = "2";
        final String EDIT_LEAVE_RECORD = "3";
        final String DELETE_LEAVE_RECORD = "4";
        final String EXIT_LEAVE_RECORD = "5";
        String choice;

        do {
            choice = getLeaveRecordChoice();
        
            switch(choice) {
                case ADD_LEAVE_RECORD:
                    addLeaveRecord();
                    break;

                case DISPLAY_LEAVE_RECORD:
                    printChoiceForLeaveRecords();
                    break;            

                case EDIT_LEAVE_RECORD:
                    validateEmployeeByLeaveRecord();
                    break;

                case DELETE_LEAVE_RECORD:
                    deleteLeaveRecord();
                    break;

                case EXIT_LEAVE_RECORD:
                     break;

                default:
                    System.out.println("Invalid input");
            } 
        }while(!choice.equals(EXIT_LEAVE_RECORD));
    }

    /**
     * Prints add, display,edit, delete and exit choice 
     * for leave records and gets input form the user
     *
     * @return choice
     */
    public String getLeaveRecordChoice() {
        StringBuilder printUserMenu = new StringBuilder();
        System.out.println(printUserMenu.append("-----Enter the choice-----\n"
                             +"1. Add\n" 
                             +"2. Display\n"
                             +"3. Edit\n"
                             +"4. Delete\n"
                             +"5. Exit\n"));
        String choice = scanner.next();
        return choice;
    }

    /**
     * Gets the leave records from the user
     * First thing it gets the enmployee id and validates  
     * the employee is exist or exist 
     * If employee exist it 
     * Gets input for fromDate, toDate and leave type
     */
    public void addLeaveRecord() {
        final int MAXIMUM_LEAVE_COUNT = 10;
        System.out.println("Enter the employee Id");
        String employeeId = scanner.next();
        Employee employee = checkEmployeeId(employeeId);       
 
        if(employee != null) {
            DateTimeUtils dateTimeUtils = new DateTimeUtils();
            System.out.println("Employee exist");

            int leaveCount = getEmployeeLeaveCount(employeeId);
            System.out.println("You have "+ (MAXIMUM_LEAVE_COUNT - leaveCount)+" left");
            System.out.println("Enter the leave from date in the format:yyyy-mm-dd");
            String fromDate = validateDate();

            System.out.println("Enter the leave to date in the format:yyyy-mm-dd");
            String toDate = validateDate();

            System.out.println("Enter the leave type\n1. Casual Leave\n"
                              +"2. Sick Leave\n3. Medical Leave");
            String choice = scanner.next();
            String leaveType = LeaveType.getLeaveType(choice).toString();
            
            String createdAt = dateTimeUtils.getDate();
            String modifiedAt = dateTimeUtils.getDate();

            LeaveRecord leaveRecord = new LeaveRecord(fromDate, toDate,
                                                  leaveType, createdAt, modifiedAt);

            if (leaveRecordServiceImpl.addLeaveRecord(leaveRecord, employeeId)) {
                System.out.println("\n\n----Leave Record not added----\n\n");
            } else {
               System.out.println("\n\n----Leave Record added successfully----\n\n");
            }
        } else {
            System.out.println("Employee not found");
        }

    }

    /**
     * Get the number of leaves taken by the employee
     * It validate the employee by employee id
     * If employee exist leave swill be calculated
     * If employee does not exist it show employee not found exception
     *
     */
    public int getEmployeeLeaveCount(String employeeId) {
        List<LeaveRecord> leaveRecords = new ArrayList<LeaveRecord>();
        LocalDate firstDate = null;
        LocalDate secondDate = null;
        int leaveCount = 0;
        try {
        leaveRecords = leaveRecordServiceImpl.getLeaveRecordByEmployeeId(employeeId);
        } catch (EmployeeNotFoundException e) {
            System.out.println(e);
        }
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        for (LeaveRecord leaveEntry :leaveRecords) {
            firstDate = dateTimeUtils.getLocalDateFormat(leaveEntry.getFromDate());
            secondDate = dateTimeUtils.getLocalDateFormat(leaveEntry.getToDate());
            int count = dateTimeUtils.findLeaveCount(firstDate, secondDate);
            leaveCount+=count; 
        }
        return leaveCount;
    }

    /**
     * Get the choice for printing Leave records
     * and calls the required methods to print leave records details 
     */
    public void printChoiceForLeaveRecords() {
        final String PRINT_LEAVE_RECORDS = "1";
        final String PRINT_LEAVE_RECORD = "2";
        System.out.println("Enter the choice");
        while(true) {
            System.out.println("1. Print all Leave Records\n"
                               +"2. Print particular Leave Record");
            String choice = scanner.next();
            if(choice.equals(PRINT_LEAVE_RECORDS)) {
                printLeaveRecords();
                break;
            } else if (choice.equals(PRINT_LEAVE_RECORD)) {
                System.out.println("Enter the employee ID");
                String employeeId = scanner.next();
                getLeaveRecordByEmployeeId(employeeId);
                break;
            } else {
                System.out.println("Enter the valid option");
            }
        }
    }    

    /**
     * Print all the leaverecords of the employees
     */
    public void printLeaveRecords() {
        StringBuilder printLeaveRecords = new StringBuilder();
        System.out.println(printLeaveRecords.append("-------------------------------------------------\n"
                              +"\n----------------LeaveRecords---------------\n\n"
                              +leaveRecordServiceImpl.getLeaveRecords()
                              +"\n-------------------------------------------------"));
    }

    /**
     * Print the leave Record by employee Id
     * 
     * @param employeeId
     */
    public void getLeaveRecordByEmployeeId(String employeeId) {
        try{
            StringBuilder printLeaveRecord = new StringBuilder();
            System.out.println(printLeaveRecord.append("-------------------------------------------------\n"
                              +"\n----------------Leave Record---------------\n\n"
                              +leaveRecordServiceImpl.getLeaveRecordByEmployeeId(employeeId)
                              +"\n-------------------------------------------------"));
        } catch (EmployeeNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
   
    /**
     * Print the choice for leave record
     * Get the input for choice from the user 
     *
     * @return choice
     */
    public String showChoiceForLeaveRecord() {
        String choice;
        StringBuilder printEditMenu = new StringBuilder();
        System.out.println(printEditMenu.append("Enter the option to change\n"
                           +"1. Change leave Start Date\n"
                           +"2. Change leave End Date \n"
                           +"3. Change Leave Type\n"
                           +"4. Exit\n"));
        choice = scanner.next();
        return choice;
    }
 
    /**
     * Validate the employee in the leave record
     */
    public void validateEmployeeByLeaveRecord() {
        LeaveRecord leaveRecord = null;
        System.out.println("Enter the employee ID");
        String employeeId = scanner.next();
        getLeaveRecordByEmployeeId(employeeId);
        List<LeaveRecord> leaveRecords = new ArrayList<LeaveRecord>();
        try {
            leaveRecords = leaveRecordServiceImpl.getLeaveRecordByEmployeeId(employeeId);
            if (leaveRecords != null) {
                editLeaveEntry(leaveRecords);
            } else {
                System.out.println("Employee does not have any leave Record");
            }
        } catch(EmployeeNotFoundException e) {
            System.out.println("Employee Not Found");
        }
    }

    /**
     * This method edit the leave records details
     * 
     * Get input from the user for updating start date, end date, leave type 
     * Every parameter is validating by validation util class
     */
    public void editLeaveEntry(List<LeaveRecord> leaveRecords) {
        final String CHANGE_START_DATE = "1";     
        final String CHANGE_END_DATE = "2";
        final String CHANGE_LEAVE_TYPE = "3";
        final String EXIT_EDIT_LEAVE_RECORD = "4";
        String choice = null;
        System.out.println("Enter the Leave ID");
        int leaveId = scanner.nextInt();

        for (LeaveRecord leaveEntry : leaveRecords) {
            if (leaveId == leaveEntry.getLeaveId()) {
                do {
                    choice = showChoiceForLeaveRecord();                              
                    switch(choice) {

                        case CHANGE_START_DATE:     
                            System.out.println("Enter the leave Start Date");
                            String userChange = validateDate();
                            leaveEntry.setFromDate(userChange);
		            updateLeaveRecord(leaveEntry);
                            break;

                        case CHANGE_END_DATE:
                            System.out.println("Enter the leave End Date");
                            userChange = validateDate();
                            leaveEntry.setToDate(userChange);
		            updateLeaveRecord(leaveEntry);
                            break;
                               
                        case CHANGE_LEAVE_TYPE:
                            System.out.println("Enter the leave type\n1. Casual Leave\n"
                                                 +"2. Sick Leave\n3. Medical Leave");
                            choice = scanner.next();
                            userChange = LeaveType.getLeaveType(choice).toString();
                            leaveEntry.setLeaveType(userChange);
                            updateLeaveRecord(leaveEntry);
                            break;
    
                        case EXIT_EDIT_LEAVE_RECORD:
                            break;

                        default:
                            System.out.println("Invalid Option");
                            break;
                    }    
                }while(!choice.equals(EXIT_EDIT_LEAVE_RECORD));   
            }
        }
    }

    /**
     * Request the update employee leave record method in employee leave record service 
     * to update the leave records
     *
     * @param employeeProject
     */
    public void updateLeaveRecord(LeaveRecord leaveEntry) {
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        leaveEntry.setModifiedAt(dateTimeUtils.getDate());
        leaveRecordServiceImpl.updateLeaveRecord(leaveEntry);
    }

    /**
     * Validate the date 
     * this method will call validation utils
     * for validation 
     *
     * @return date
     */
    public String validateDate() {
        ValidationUtils validationUtils = new ValidationUtils();
        String date;
        boolean isValidDate = false;
        do {
            date = scanner.next();
            isValidDate = validationUtils.validateDate(date);

            if (isValidDate == false) {
                System.out.println("Please enter the valid date");    
            }
        }while(isValidDate == false);
        return date;
    }

    /**
     * Gets the employee id which the user 
     * wants to remove the leave record the data
     */
    public void deleteLeaveRecord() {
        System.out.println("Enter the employee Id");
        String employeeId = scanner.next();
        int deleteEmployee = leaveRecordServiceImpl.removeLeaveRecord(employeeId);
        if (deleteEmployee > 0) {
            System.out.println("\n\n-----Leave Records Deleted Successfully------\n\n");
        } else {
            System.out.println("\n\n-----Leave Records not found-----\n\n");
        }        
    }

/*------------------------Employee Project*----------------------------*/


    /**
     * prints the choice for managing employee project
     * and invokes other methods based on choice
     */
    public void chooseOperationForEmployeeProject() {
        final String ADD_EMPLOYEE_PROJECT = "1";
        final String DISPLAY_EMPLOYEE_PROJECT = "2";
        final String UPDATE_EMPLOYEE_PROJECT = "3";
        final String DELETE_EMPLOYEE_PROJECT = "4";
        final String EXIT_EMPLOYEE_PROJECT = "5";
        String choice;

        do {
            choice = getChoiceForEmployeeProject();
        
            switch(choice) {
                case ADD_EMPLOYEE_PROJECT:
                    addEmployeeProject();
                    break;

                case DISPLAY_EMPLOYEE_PROJECT:
                    printChoiceForEmployeeProject();
                    break;            
         
                case UPDATE_EMPLOYEE_PROJECT:
                    validateEmployeeProject(); 
                    break; 

                case DELETE_EMPLOYEE_PROJECT:
                    deleteEmployeeProject();
                    break;        
 
                case EXIT_EMPLOYEE_PROJECT:
                     break;
          
                default:
                    System.out.println("Invalid input");
            } 
        }while(!choice.equals(EXIT_EMPLOYEE_PROJECT));
    }

    /**
     * Prints add, display,edit, delete and exit choice 
     * for employee project and gets input form the user
     *
     * @return choice
     */
    public String getChoiceForEmployeeProject() {
        StringBuilder printUserMenu = new StringBuilder();
        System.out.println(printUserMenu.append("-----Enter the choice-----\n"
                             +"1. Add\n" 
                             +"2. Display\n"
                             +"3. Update\n"
                             +"4. Delete\n"
                             +"5. Exit\n"));
        String choice = scanner.next();
        return choice;
    }

    /**
     * Gets the employee project from the user
     * First thing it gets the enmployee id and validates  
     * the employee is exist or exist 
     * If employee exist it 
     * Gets input for project name, project manager id and start date of project
     */
    public void addEmployeeProject() {
        System.out.println("Enter the employee Id");
        String employeeId = scanner.next();
        Employee employee = checkEmployeeId(employeeId);       
 
        if(employee != null) {
            System.out.println("Employee exist\nEnter the Project Manager Id");
            String projectManagerId = scanner.next();
            employee = checkEmployeeId(employeeId);       
 
        if(employee != null) {
                String projectManager = employee.getEmployeeName();

                DateTimeUtils dateTimeUtils = new DateTimeUtils();

                System.out.println("Employee exist");
                System.out.println("Enter the Project Name");
                String projectName = validateName();

                System.out.println("Enter the Client Name");
                String clientName = validateName();
 
                System.out.println("Enter the Project start date");
                String startDate = validateDate();

                String createdAt = dateTimeUtils.getDate();
                String modifiedAt = dateTimeUtils.getDate();

                EmployeeProject employeeProject = new EmployeeProject(projectName, 
                                                                      projectManager, 
                                                                      clientName, 
                                                                      startDate,
                                                                      createdAt,
                                                                      modifiedAt);
             // int project = employeeProjectServiceImpl.addEmployeeProject(employeeProject, employeeId);
                if (employeeProjectServiceImpl.addEmployeeProject(employeeProject, employeeId)) {
                    System.out.println("\n\n----Employee Project not added----\n\n");
                } else {
                   System.out.println("\n\n----Employee Project added successfully----\n\n");
                }
            }
        }

    }

    /**
     * Get the choice for printing employee project details
     * and calls the required methods to print employee projects details 
     */
    public void printChoiceForEmployeeProject() {
        final String PRINT_ALL_EMPLOYEE_PROJECTS = "1";
        final String PRINT_EMPLOYEE_PROJECT = "2";
        System.out.println("Enter the choice");
        while(true) {
            System.out.println("1. Print all Employee Projects\n"
                               +"2. Print particular Employee Project");
            String choice = scanner.next();
            if(choice.equals(PRINT_ALL_EMPLOYEE_PROJECTS)) {
                printEployeeProjects();
                break;
            } else if (choice.equals(PRINT_EMPLOYEE_PROJECT)) {
                getEmployeeProjectByEmployeeId();
                break;
            } else {
                System.out.println("Enter the valid option");
            }
        }
    }    

    /**
     * Print all the employee projects of the employees
     */
    public void printEployeeProjects() {
        StringBuilder printEmployeeProjects = new StringBuilder();
        System.out.println(printEmployeeProjects.append("-------------------------------------------------\n"
                              +"\n----------------Employee Projects---------------\n\n"
                              +employeeProjectServiceImpl.getEmployeeProjects()
                              +"\n-------------------------------------------------"));
    }

    /**
     * Print the Employee Project by employee Id
     * 
     * @param employeeId
     */
    public void getEmployeeProjectByEmployeeId() {
        try{
            System.out.println("Enter the employee ID");
            String employeeId = scanner.next();
            StringBuilder printEmployeeProject = new StringBuilder();
            System.out.println(printEmployeeProject.append("-------------------------------------------------\n"
                              +"\n----------------Employee Project---------------\n\n"
                              +employeeProjectServiceImpl.getEmployeeProjectByEmployeeId(employeeId)
                              +"\n-------------------------------------------------"));
        } catch (EmployeeNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Print the choice for employee project
     * Get the input for choice from the user 
     *
     * @return choice
     */
    public String showChoiceForEmployeeProject() {
        String choice;
        StringBuilder printEditMenu = new StringBuilder();
        System.out.println(printEditMenu.append("Enter the option to change\n"
                           +"1. Change Project name\n"
                           +"2. Change Project manager \n"
                           +"3. Change Client Name\n"
                           +"4. Change Project Start Date\n"
                           +"5. Exit\n"));
        choice = scanner.next();
        return choice;
    }

    /**
     * validate the employee in the Project details
     * then if user want to change employee id 
     * it will get the new employee id and validate whether
     * the employee is exist or not
     *  
     */
    public void validateEmployeeProject() {
        final String CHANGE_EMPLOYEE_ID = "1";
        final String NO_CHANGE = "2";
        EmployeeProject employeeProject = null;
        String employeeId = " ";
        String newEmployeeID = " ";
        System.out.println("Enter the employee Id");
        employeeId = scanner.next(); 
        employeeProject = checkEmployeeProjectId(employeeId);
        do {        
            System.out.println("Do you want to change employee ID\n1. Yes\n2. No");
            String choice = scanner.next();
            if (choice.equals(CHANGE_EMPLOYEE_ID)) {

                if (employeeProject != null) { 
                    System.out.println("Employee exist");
                    System.out.println("Enter the new employee ID");
                    newEmployeeID = scanner.next();
                    employeeProject = checkEmployeeProjectId(employeeId);

                    if (employeeProject != null) {  
                        System.out.println("Employee exist");
                        editEmployeeProject(employeeProject);
                    } else {
                        System.out.println("---Employee Not Found---");
                    }

                } else {
                    System.out.println("---Employee Not Found---");
                }

            } else if (choice.equals(NO_CHANGE)) {
                System.out.println("Employee exist");
                editEmployeeProject(employeeProject);
                break;
            } else {
                 System.out.println("Invalid Choice");
            }
        }while(true);
    }

    /**
     * This method edit the employee project details
     * 
     * Get input from the user for updating start date of project
     * project name, client name 
     * Every parameter is validating by validation util class
     */
    public void editEmployeeProject(EmployeeProject employeeProject) {
        final String CHANGE_PROJECT_NAME = "1";     
        final String CHANGE_PROJECT_MANAGER = "2";
        final String CHANGE_CLIENT_NAME = "3";
        final String CHANGE_PROJECT_START_DATE = "4";
        final String EXIT_EMPLOYEE_PROJECT_EDIT = "5";
        String userChange = " ";
        String choice = " ";
        if (employeeProject != null) {
            do {
                choice = showChoiceForEmployeeProject();                              
                switch(choice) {

                    case CHANGE_PROJECT_NAME:     
                        System.out.println("Enter the new project name");
                        userChange = validateName();
                        employeeProject.setProjectName(userChange);
		        updateEmployeeProject(employeeProject);
                        break;

                    case CHANGE_PROJECT_MANAGER:
                        System.out.println("Enter the new project Manager Id");
                        userChange = scanner.next();
                        Employee employee = checkEmployeeId(userChange);       
                        if(employee != null) {
                            userChange = employee.getEmployeeName();
                            employeeProject.setProjectManager(userChange);
		            updateEmployeeProject(employeeProject);
                        } else {
                            System.out.println("Invalid Id");
                        }
                        break;
                               
                    case CHANGE_CLIENT_NAME:
                        System.out.println("Enter the new Client Name"); 
                        userChange = validateName();
                        employeeProject.setClientName(userChange);
                        updateEmployeeProject(employeeProject);
                        break;

                    case CHANGE_PROJECT_START_DATE:
                        System.out.println("Enter the new project start date");
                        userChange = validateDate();
                        employeeProject.setStartDate(userChange);
                        updateEmployeeProject(employeeProject);
                        break;
    
                    case EXIT_EMPLOYEE_PROJECT_EDIT:
                        break;

                    default:
                        System.out.println("Invalid Option");
                        break;
                }    
            }while(!choice.equals(EXIT_EMPLOYEE_PROJECT_EDIT));   
        }
    }

    /**
     * Validate whether the employee in project is exist or not
     *
     */ 
    public EmployeeProject checkEmployeeProjectId(String employeeId) {
        List<EmployeeProject> employeeProjects = new ArrayList<EmployeeProject>();
        employeeProjects = employeeProjectServiceImpl.getEmployeeProjectByEmployeeId(employeeId);
        for (EmployeeProject project : employeeProjects) {
            if (employeeId == project.getProjectId()) {
        return employeeProject;
    }

    /**
     * Request the update employee project method in employee project service 
     * to update the project details
     *
     * @param employeeProject
     */
    public void updateEmployeeProject(EmployeeProject employeeProject) {
        DateTimeUtils dateTimeUtils = new DateTimeUtils();
        employeeProjectServiceImpl.updateEmployeeProject(employeeProject);
        employeeProject.setModifiedAt(dateTimeUtils.getDate());
        employeeProjectServiceImpl.updateEmployeeProject(employeeProject);
    }

    /**
     * Gets the employee id which the user 
     * wants to remove the employee Project the data
     */
    public void deleteEmployeeProject() {
        System.out.println("Enter the employee Id");
        String employeeId = scanner.next();

        if (employeeProjectServiceImpl.removeEmployeeProject(employeeId)) {
            System.out.println("\n\n-----Employee not found------\n\n");
        } else {
            System.out.println("\n\n-----Employee Deleted Successfully-----\n\n");
        }        
    }
}