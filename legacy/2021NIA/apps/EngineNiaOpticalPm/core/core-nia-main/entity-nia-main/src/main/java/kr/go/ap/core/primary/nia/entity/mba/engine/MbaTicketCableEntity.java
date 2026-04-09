package kr.go.ap.core.primary.nia.entity.mba.engine;

import jakarta.persistence.*;
import kr.go.ap.core.primary.nia.entity.mba.engine.key.MbaTicketCableKey;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "TB_MBA_TICKET_CABLE",schema="MBA")
public class MbaTicketCableEntity implements Persistable<MbaTicketCableKey> {


    @EmbeddedId
    private MbaTicketCableKey mbaTicketCableKey;

    @Column(name = "is_root_row_signal_line")
    private boolean isRootRowSignalLine;

    @Column(name = "sysnamea")
    private String sysnamea;
    @Column(name = "sysnamez")
    private String sysnamez;

    @Column(name = "ptpnamea")
    private String ptpnamea;
    @Column(name = "ptpnamez")
    private String ptpnamez;
    @Column(name = "tida")
    private String tida;
    @Column(name = "tidz")
    private String tidz;


    @Transient
    private boolean isNew = true;




    @Override
    public MbaTicketCableKey getId() {
        return mbaTicketCableKey;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PrePersist
    @PostLoad
    public void markNotNew() {
        this.isNew = false;
    }

    public void setTidz(String tidz) {
        this.tidz = tidz;
    }
    public void setSysnamez(String sysnamez) {
        this.sysnamez = sysnamez;
    }

    public void setPtpnamez(String ptpnamez) {
        this.ptpnamez = ptpnamez;
    }


}
