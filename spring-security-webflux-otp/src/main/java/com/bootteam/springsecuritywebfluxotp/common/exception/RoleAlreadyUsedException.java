package com.bootteam.springsecuritywebfluxotp.common.exception;

public class RoleAlreadyUsedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RoleAlreadyUsedException() {
        super("Role name is already exist!");
    }
}
