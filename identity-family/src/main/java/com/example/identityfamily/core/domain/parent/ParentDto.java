package com.example.identityfamily.core.domain.parent;


import com.example.identityfamily.core.domain.exception.PhoneNumberNotValid;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParentDto {

    private Long id;
    @NotBlank(message = "firstName is required")
    private String firstName;
    @NotBlank(message = "lastName is required")
    private String lastName;
    @NotBlank(message = "phoneNumber is required")
    @Size(max = 12)
    private String phoneNumber;
    private long user_id;


    @PrePersist
    private String formatPhoneNumber(String phoneNumber) {

        if (phoneNumber.length() != 10) {
            throw new PhoneNumberNotValid();
        }

        if (phoneNumber.startsWith("0")) {
            phoneNumber = "213" + phoneNumber.substring(1);
        }

        return phoneNumber;
    }


}
