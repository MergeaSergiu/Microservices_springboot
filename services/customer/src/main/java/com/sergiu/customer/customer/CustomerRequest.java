package com.sergiu.customer.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        String id,

        @NotNull(message = "First name required")
        String firstName,

        @NotNull(message = "Last name required")
        String lastName,

        @NotNull(message = "Email required")
                @Email(message = "Not a valid email address")
        String email,


        Address address) {
}
