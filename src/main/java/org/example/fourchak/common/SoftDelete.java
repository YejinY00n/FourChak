package org.example.fourchak.common;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.example.fourchak.common.error.CustomRuntimeException;
import org.example.fourchak.common.error.ExceptionCode;

@Setter
@Getter
@MappedSuperclass
public abstract class SoftDelete extends BaseEntity{

    private boolean isDeleted;

    public void isDelete() {
        if(this.isDeleted) {
            throw new CustomRuntimeException(ExceptionCode.ALREADY_DELETED_USER);
        }

        this.isDeleted = true;
    }
}
