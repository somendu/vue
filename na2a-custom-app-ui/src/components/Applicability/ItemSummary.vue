<template>
  <table class="table table-sm" id="is-summaryTable">
    <thead class="thead-light">
      <tr id="is-headerRow">
        <th>ID</th>
        <th>Name</th>
        <th>Short description</th>
      </tr>
    </thead>

    <tbody v-if="details">
      <tr v-for="(detail, idx) of details" :key="idx" :id="'is-row-' + idx">
        <td :id="'is-detailId-' + idx">{{ detail.id }}</td>
        <td :id="'is-detailProductNumber-' + idx">{{ detail.productNumber }}</td>
        <td :id="'is-detailDescr-' + idx">{{ detail.shortDescription }}</td>
      </tr>
    </tbody>
  </table>
</template>

<script>
export default {
  props: {
    details: {
      required: true
    }
  }
};
</script>
