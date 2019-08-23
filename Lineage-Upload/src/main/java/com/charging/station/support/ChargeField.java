package com.charging.station.support;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.annotation.Scope;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * Support Bean class
 * 
 * @author somendu
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

@Scope("request")
// Added this for ensuring thread-safety as for each request it creates new object. 
//scope = prototype can also be used
public class ChargeField {

	private UUID id;
	private String stationId;
	private LocalDateTime startedAt;
	private LocalDateTime stoppedAt;
	private StatusEnum status;

	public ChargeField copyBean(ChargeField chargeField) {

		ChargeField chargeFieldNew = new ChargeField();
		chargeFieldNew.setId(chargeField.getId());
		chargeFieldNew.setStationId(chargeField.getStationId());
		chargeFieldNew.setStartedAt(chargeField.getStartedAt());
		chargeFieldNew.setStoppedAt(chargeField.getStoppedAt());
		chargeFieldNew.setStatus(chargeField.getStatus());

		return chargeFieldNew;
	}

}
