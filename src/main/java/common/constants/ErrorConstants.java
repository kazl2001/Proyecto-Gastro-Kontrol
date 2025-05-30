package common.constants;

public class ErrorConstants {



    public ErrorConstants() {
    }

    //ERROR MESSAGES
    public static final String DATA_RETRIEVAL_ERROR_NOT_FOUND = "The data for the orders of this customer was not found. ";
    public static final String ENTITY_NOT_FOUND_ERROR = "The entity was not found. ";
    public static final String INVALID_ENTITY_DATA_ERROR = "Invalid entity data. The credentials are incorrect.";
    public static final String INVALID_DATA_FORMAT_ERROR = "Invalid data format. ";
    public static final String GENERAL_DATABASE_ERROR = "Error while retrieving data from the database. ";
    public static final String FOREIGN_KEY_CONSTRAINT_ERROR = "Foreign key constraint error. ";
    public static final String NO_ROWS_AFFECTED = "No rows affected in database. ";
    public static final String NO_GENERATED_KEY = "Key was not generated. ";
    public static final String CONFIRMATION_NEEDED_ERROR = "Confirmation needed. ";
    public static final String UNIQUE_FIELD_CONSTRAINT_ERROR = "This username is taken. Please choose another one. ";
    public static final String ERROR_LOADING_IMAGE = "Error loading the image. ";
    public static final String ERROR_SAVING_PHONE = "The phone number must be exactly 9 digits, try again. ";
    public static final String INCORRECT_PASSWORD = "Invalid entity data. The password is incorrect.";
    public static final String USER_NOT_FOUND = "User not found in the database.";


    //ERROR NUMERIC CODES
    public static final int UNIQUE_FIELD_CONSTRAINT_ERROR_CODE = 1062;
    public static final int CONFIRMATION_NEEDED_ERROR_CODE = 999;
    public static final int GENERAL_DATABASE_ERROR_CODE = 1000;
    public static final int DATA_RETRIEVAL_ERROR_NOT_FOUND_ERROR_CODE = 1003;
    public static final int NO_ROWS_AFFECTED_ERROR_CODE = 1004;
    public static final int NO_GENERATED_KEY_ERROR_CODE = 1005;
    public static final int ENTITY_NOT_FOUND_ERROR_CODE = 2000;
    public static final int INVALID_ENTITY_DATA_ERROR_CODE = 2002;
    public static final int INVALID_DATA_FORMAT_ERROR_CODE = 3002;
    public static final int OPERATION_SUCCESSFUL_CODE = 0;
    public static final int CODE_ERROR_LOADING_IMAGE = 50;

}
