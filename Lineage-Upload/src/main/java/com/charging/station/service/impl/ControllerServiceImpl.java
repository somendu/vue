/**
 * 
 */
package com.charging.station.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.charging.station.service.ControllerService;
import com.charging.station.support.ChargeField;
import com.charging.station.support.ChargeFieldCount;
import com.charging.station.support.ChargeFieldsMap;
import com.charging.station.support.ChargeRequest;
import com.charging.station.support.StatusEnum;

/**
 * Service layer implementation class for handling all business logic
 * 
 * @author somendu
 *
 */
@Service

public class ControllerServiceImpl implements ControllerService {

	@Override
	public ChargeField startChargingSession(ChargeRequest request) {

		ChargeFieldsMap.clearList();

		List<ChargeField> chargeFieldList = ChargeFieldsMap.getChargeFieldMap();

		ChargeField chargeFieldInProgress = null;
		ChargeField chargeFieldNew = null;

		String existingStationId = "";

		for (ChargeField chargeFieldStop : chargeFieldList) {

			if (chargeFieldStop.getStationId().equalsIgnoreCase(request.getStationId())) {
				System.out.println(" Station ID Found" + chargeFieldStop.getStationId());
				existingStationId = chargeFieldStop.getStationId();
				chargeFieldInProgress = new ChargeField();
				chargeFieldInProgress.setId(UUID.randomUUID());
				chargeFieldInProgress.setStationId(request.getStationId());
				chargeFieldInProgress.setStartedAt(LocalDateTime.now());
				chargeFieldInProgress.setStatus(StatusEnum.IN_PROGRESS);

			}
			// TODO
			// Can handle null here for 'No Station ID Found', but assuming to be
			// hypothetical only, as there should be no stations without station id.

		}

		if (existingStationId.equalsIgnoreCase("")) {
			System.out.println(" Station ID Creating new : " + request.getStationId());

			chargeFieldNew = new ChargeField();
			chargeFieldNew.setId(UUID.randomUUID());
			chargeFieldNew.setStationId(request.getStationId());
			chargeFieldNew.setStartedAt(LocalDateTime.now());
			chargeFieldNew.setStatus(StatusEnum.IN_PROGRESS);
		}

		if (chargeFieldNew != null) {
			System.out.println("chargeFieldNew is not null");
			ChargeFieldsMap.addData(chargeFieldNew);
		}

		if (chargeFieldInProgress != null) {
			return chargeFieldInProgress;
		} else {
			return chargeFieldNew;
		}

	}

	@Override
	public ChargeField stopChargingSession(String stationID) {

		List<ChargeField> chargeFieldList = ChargeFieldsMap.getChargeFieldMap();

		List<ChargeField> chargeFieldInProgressList = new ArrayList<ChargeField>();

		for (ChargeField chargeField : chargeFieldList) {
			if (chargeField.getStatus() == StatusEnum.IN_PROGRESS) {
				chargeFieldInProgressList.add(chargeField);
			}
		}

		ChargeField chargeField = new ChargeField();

		for (ChargeField chargeFieldStop : chargeFieldInProgressList) {

			if (chargeFieldStop.getStationId().equalsIgnoreCase(stationID)) {
				chargeFieldStop.setStatus(StatusEnum.FINISHED);
				chargeFieldStop.setStoppedAt(LocalDateTime.now());

				chargeField = chargeFieldStop.copyBean(chargeFieldStop);

			}

		}

		return chargeField;
	}

	@Override
	public List<ChargeField> getChargingSessions() {

		List<ChargeField> chargeFields = ChargeFieldsMap.getChargeFieldMap();

		return chargeFields;

	}

	@Override
	public ChargeFieldCount getChargingSummary() {

		List<ChargeField> chargeFields = ChargeFieldsMap.getChargeFieldMap();

		int totalCount = chargeFields.size();
		int startCount = 0;
		int stopCount = 0;

		for (ChargeField chargeField : chargeFields) {

			if (chargeField.getStatus() == StatusEnum.FINISHED) {
				stopCount++;
			} else {
				if (chargeField.getStatus() == StatusEnum.IN_PROGRESS) {
					startCount++;
				}
			}
		}

		ChargeFieldCount chargeFieldCount = new ChargeFieldCount();
		chargeFieldCount.setTotalCount(totalCount);
		chargeFieldCount.setStartedCount(startCount);

		chargeFieldCount.setStoppedCount(stopCount);

		return chargeFieldCount;
	}

}
