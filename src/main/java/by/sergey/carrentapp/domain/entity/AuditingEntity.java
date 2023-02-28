package by.sergey.carrentapp.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditingEntity<T extends Serializable> implements BaseEntity<T>{

    @Column(nullable = false)
    @Version
    @LastModifiedDate
    private Instant modifiedAt;
    @LastModifiedBy
    private String modifiedBy;

    @Column(updatable = false)
    @CreatedDate
    private Instant createdAt;
    @Column(updatable = false)
    @CreatedBy
    private String createdBy;

}
