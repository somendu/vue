package com.vqd.tme.na2a.partlinking.persistence;

import com.vqd.tme.na2a.exception.applicability.CouldNotSaveException;
import com.vqd.tme.na2a.exception.applicability.SavedWithErrorsException;
import com.vqd.tme.na2a.partlinking.model.Part;
import com.vqd.tme.na2a.partlinking.model.PartsFilter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Service for finding parts by id / search query
 *
 * @author edgardegraaff
 *
 */
public interface Parts {
  /**
   * Resolve these parts. Returns a map in which the identifier is the key.
   * In case a part could not be found, it'll be absent in the map.
   *
   * @param identifiers
   * @return
   */
  Map<String, Part> get(Collection<String> identifiers);

  /**
   * Resolve these parts. Returns a map in which the identifier is the key.
   * In case a part could not be found, it'll be absent in the map.
   *
   * @param identifier
   * @return
   */
  Map<String, Part> get(String identifier);

//  /**
//   * Finds parts by code / name substring match
//   *
//   * @param partNumber
//   * @Param partName
//   * @Param characteristics
//   * @return
//   */
//  List<Part> search(String partNumber, String partName, String characteristics);

  /**
   * Finds parts by filter values
   *
   * @param filter
   * @return
   */
  List<Part> search(PartsFilter filter);

  /**
   * Removes a reference between a part and variant. Note that the part itself is not deleted.
   *
   */
  void delete(String variantId, String partId, String partType) throws CouldNotSaveException;

  void createNPA(Part part) throws CouldNotSaveException;

}
