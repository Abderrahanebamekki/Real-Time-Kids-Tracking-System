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



    public String formatPhoneNumber() {

        if (this.phoneNumber.length() != 10) {
            throw new PhoneNumberNotValid();
        }

        if (this.phoneNumber.startsWith("0")) {
            this.phoneNumber = "213" + this.phoneNumber.substring(1);
        }

        return phoneNumber;
    }


}
