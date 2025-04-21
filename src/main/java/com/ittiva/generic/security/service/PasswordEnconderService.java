/**
 * 
 */
package com.ittiva.generic.security.service;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author ITTIVA
 *
 */
public class PasswordEnconderService implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return charSequence.toString().equals(s);
    }

}
