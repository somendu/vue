/**
 * 
 */
package com.charging.station.support;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;

/**
 * 
 * In-memory data - hard coded values
 * 
 * @author somendu
 *
 */
@Data
public class ChargeFieldsMap {

	private static List<ChargeField> chargeFieldArray = new ArrayList<ChargeField>();

	public static void clearList() {
		chargeFieldArray.clear();
	}

	public static List<ChargeField> getChargeFieldMap() {

		// Filed 1 - In Progress
		ChargeField chargeField = new ChargeField();

		chargeField.setId(UUID.randomUUID());
		chargeField.setStationId("A-19820");

		LocalDateTime localDateTimeStartedAt = LocalDateTime.of(2019, 8, 13, 12, 24);
		LocalDateTime localDateTimeStoppedAt = localDateTimeStartedAt.plusMinutes(100);

		chargeField.setStartedAt(localDateTimeStartedAt);
		chargeField.setStoppedAt(localDateTimeStoppedAt);
		chargeField.setStatus(StatusEnum.IN_PROGRESS);

		// Field 2 - In progress
		ChargeField chargeFieldOne = new ChargeField();

		chargeFieldOne.setId(UUID.randomUUID());
		chargeFieldOne.setStationId("A-19821");

		LocalDateTime localDateTimeStartedAtOne = LocalDateTime.of(2019, 8, 13, 13, 56);
		LocalDateTime localDateTimeStoppedAtOne = localDateTimeStartedAt.plusMinutes(100);

		chargeFieldOne.setStartedAt(localDateTimeStartedAtOne);
		chargeFieldOne.setStoppedAt(localDateTimeStoppedAtOne);
		chargeFieldOne.setStatus(StatusEnum.IN_PROGRESS);

		// Field 3 - In Progress
		ChargeField chargeFieldTwo = new ChargeField();

		chargeFieldTwo.setId(UUID.randomUUID());
		chargeFieldTwo.setStationId("A-19822");

		LocalDateTime localDateTimeStartedAtTwo = LocalDateTime.of(2019, 8, 13, 18, 12);
		LocalDateTime localDateTimeStoppedAtTwo = localDateTimeStartedAt.plusMinutes(100);

		chargeFieldTwo.setStartedAt(localDateTimeStartedAtTwo);
		chargeFieldTwo.setStoppedAt(localDateTimeStoppedAtTwo);
		chargeFieldTwo.setStatus(StatusEnum.IN_PROGRESS);

		// Field 4 - Finished
		ChargeField chargeFieldThree = new ChargeField();

		chargeFieldThree.setId(UUID.randomUUID());
		chargeFieldThree.setStationId("A-19823");

		LocalDateTime localDateTimeStartedAtThree = LocalDateTime.of(2019, 8, 13, 19, 20);
		LocalDateTime localDateTimeStoppedAtThree = localDateTimeStartedAt.plusMinutes(100);

		chargeFieldThree.setStartedAt(localDateTimeStartedAtThree);
		chargeFieldThree.setStoppedAt(localDateTimeStoppedAtThree);
		chargeFieldThree.setStatus(StatusEnum.FINISHED);

		// Field 5 - Finished
		ChargeField chargeFieldFour = new ChargeField();

		chargeFieldFour.setId(UUID.randomUUID());
		chargeFieldFour.setStationId("A-19824");

		LocalDateTime localDateTimeStartedAtFour = LocalDateTime.of(2019, 8, 13, 21, 50);
		LocalDateTime localDateTimeStoppedAtFour = localDateTimeStartedAt.plusMinutes(100);

		chargeFieldFour.setStartedAt(localDateTimeStartedAtFour);
		chargeFieldFour.setStoppedAt(localDateTimeStoppedAtFour);
		chargeFieldFour.setStatus(StatusEnum.FINISHED);

		chargeFieldArray.add(chargeField);
		chargeFieldArray.add(chargeFieldOne);
		chargeFieldArray.add(chargeFieldTwo);
		chargeFieldArray.add(chargeFieldThree);
		chargeFieldArray.add(chargeFieldFour);

		return chargeFieldArray;

	}

	public static void addData(ChargeField chargeField) {
		chargeFieldArray.add(chargeField);

		for (ChargeField chargeFieldDisp : chargeFieldArray) {
			System.out.println("Charge Field Values : " + chargeFieldDisp.toString() + "\n");
		}
	}

}
