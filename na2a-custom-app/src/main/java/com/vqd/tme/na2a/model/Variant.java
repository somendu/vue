package com.vqd.tme.na2a.model;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain=true)
@ToString(exclude="product")
@EqualsAndHashCode(exclude="product")
public class Variant {
  /**
   * Selected options for this variant. This is extracted to a separate
   * class, so that equals / hashCode help recognizing similar selections
   * over multiple variants.
   *
   * @author edgardegraaff
   *
   */
  @Data
  @Accessors(chain=true)
  @EqualsAndHashCode(exclude={"cancelledReason"})
  public static class Options {
//    private boolean cancelled;
    private String statusCode;
    private String cancelledReason;

    private String brand;
    private String model;
    private String generation;

    private List<String> subProjectIds;
    private List<String> subProjects;
    private List<String> bodyTypeIds;
    private List<String> bodyTypes;
    private List<String> engineIds;
    private List<String> engines;
    private List<String> transmissionIds;
    private List<String> transmissions;
    private List<String> productionGradeIds;
    private List<String> productionGrades;
    private List<String> productionLocationIds;
    private List<String> productionLocations;
    private String steeringId;
    private String steering;

    private List<String> factoryEquipmentIds;
    private List<String> factoryEquipments;
    private List<String> interiorColourIds;
    private List<String> interiorColours;
    private List<String> exteriorColourIds;
    private List<String> exteriorColours;
    private List<String> trimColourIds;
    private List<String> trimColours;
    private List<String> countries;

    private String organisation;
    private List<String> localProject;
    private List<String> localModel;
    private List<String> localGrade;
    private List<String> localModelCode;

    /**
     * Adds factory equipment option, creating lists if necessary
     * @param id
     * @param value
     */
    public void addFactoryEquipment(String id, String value) {
      if (factoryEquipmentIds == null) {
        factoryEquipmentIds = new ArrayList<>();
      }
      if (factoryEquipments == null) {
        factoryEquipments = new ArrayList<>();
      }
      factoryEquipmentIds.add(id);
      factoryEquipments.add(value);
    }

    public void addCountry(String id) {
        if (countries == null) {
            countries = Lists.newArrayList();
        }
        countries.add(id);
    }

    /**
     * Adds colour, creates lists if necessary
     *
     * @param id
     * @param value
     */
    public void addColour(String id, String value, String type) {
      if (type.equals("interior")) {
          interiorColourIds = addColour(id, interiorColourIds);
          interiorColours = addColour(value, interiorColours);
      } else if (type.equals("exterior")) {
          exteriorColourIds = addColour(id, exteriorColourIds);
          exteriorColours = addColour(value, exteriorColours);
      } else if(type.equals("trim")) {
          trimColourIds = addColour(id, trimColourIds);
          trimColours = addColour(value, trimColours);
      }
    }

    private List<String> addColour(String value, List<String> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(value);
        return list;
    }
  }

  private Product product;
  private String id;
  private String variantNo;
  private String shortDescription;
  private List<String> katashiki;
  private Options options;
}
