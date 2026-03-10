package kr.go.ap.code.pm.ticket;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum TicketReasonCode {

    NODE_TOTAL_DEVIATION_CHANGE("Node Total Deviation 변화","Node Total Deviation 변화"),
    OPTICAL_LEVEL_LONG_TERM_VARIATION_ALERT("광레벨 장기 변화 예측 경보","광레벨 장기 변화 예측 경보");

    private String code;
    private String name;

    TicketReasonCode(final String code, final String name) {
        this.name = name;
        this.code = code;
    }


}
