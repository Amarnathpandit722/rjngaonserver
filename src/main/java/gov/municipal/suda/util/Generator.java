package gov.municipal.suda.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Generator {
    /*
        StringBuilder newTransactionNoBuilder= new StringBuilder();
            newTransactionNoBuilder.append(transactionMasterResult.getId().toString());
            String dashFormatDate=dashTypeDateFormatter.format(LocalDate.now());
            String dashFormatDateWithoutDash=dashFormatDate.replaceAll("\\s?-\\s?", "");
            newTransactionNoBuilder.append(dashFormatDateWithoutDash);
            String currentTime=LocalTime.now().toString();
            String removeColonFromTime=currentTime.replaceAll("\\s?:\\s?", "");
            int dotIndex=removeColonFromTime.indexOf(".");
            String finalCurrentTime=null;
            if(dotIndex !=-1) {
                finalCurrentTime=removeColonFromTime.substring(0,dotIndex);
            }
            newTransactionNoBuilder.append(finalCurrentTime);
     */
    public static final  String generateTransactionNumber(Long id) {
        StringBuilder newTransactionNoBuilder= new StringBuilder();
        DateTimeFormatter dashTypeDateFormatter= DateTimeFormatter.ofPattern("dd-MM-YYYY");
        newTransactionNoBuilder.append(id.toString());
        String dashFormatDate=dashTypeDateFormatter.format(LocalDate.now());
        String dashFormatDateWithoutDash=dashFormatDate.replaceAll("\\s?-\\s?", "");
        newTransactionNoBuilder.append(dashFormatDateWithoutDash);
        String currentTime= LocalTime.now().toString();
        String removeColonFromTime=currentTime.replaceAll("\\s?:\\s?", "");
        int dotIndex=removeColonFromTime.indexOf(".");
        String finalCurrentTime=null;
        if(dotIndex !=-1) {
            finalCurrentTime=removeColonFromTime.substring(0,dotIndex);
        }
        newTransactionNoBuilder.append(finalCurrentTime);

        return newTransactionNoBuilder.toString();
    }
}
