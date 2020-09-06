package fr.rushcubeland.rcbapi.data.exceptions;

import java.util.UUID;

public class AccountNotFoundException extends Exception {

    public AccountNotFoundException(UUID uuid){
        super("The account (" + uuid.toString() + ") cannot be found");
    }
}
