package com.example.th02201.model;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Table(name = "chat_lieu")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Data
public class ChatLieu {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "ten_chat_lieu", nullable = false, length = 100)
        private String tenChatLieu;

        @Column(name = "ma_chat_lieu", nullable = false, length = 20, unique = true)
        private String maChatLieu;

        @Column(name = "ngay_tao", nullable = false, updatable = false)
        private LocalDateTime ngayTao;

        @Column(name = "ngay_cap_nhat", nullable = false)
        private LocalDateTime ngayCapNhat;

        @Transient
        private Integer soLuongSanPham;

        @OneToMany(mappedBy = "chatLieu", cascade = CascadeType.ALL, orphanRemoval = true)
        @EqualsAndHashCode.Exclude // Exclude from equals and hashCode
        private Set<ChiTietSanPham> chiTietSanPhams; // Assuming ChatLieu has a collection of ChiTietSanPham

        @PrePersist
        protected void onCreate() {
                this.ngayTao = LocalDateTime.now();
                this.ngayCapNhat = LocalDateTime.now();
        }

        @PreUpdate
        protected void onUpdate() {
                this.ngayCapNhat = LocalDateTime.now();
        }
}
