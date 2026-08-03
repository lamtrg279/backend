package com.printledger.backend.entity;

import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "job_parts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String unitUOM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job", nullable = false)
    private Job job;

    @OneToMany(mappedBy = "jobPart", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<JobMaterial> jobMaterials = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;
}
