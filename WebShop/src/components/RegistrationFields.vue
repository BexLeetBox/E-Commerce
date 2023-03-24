<template id="template">
    <label :for="uuid" v-if="label">
      {{ label }}
    </label>
    <input
      class="inputField"
      v-bind="{
        ...$attrs,
        onInput: updateValue,
      }"
      :id="uuid"
      :value="modelValue"
      :placeholder="label"
      :aria-describedby="error ? '${uuid}-error' : null"
      :aria-invalid="!!error"
      :class="{ error }"
      @input="$emit('update:modelValue', $event.target.value)"
    />
  </template>
  
  <script>
  import UniqueId from "../util/UniqueId.js";
  import SetupFormComponent from "../util/FormSetup.js";
  export default {
    name: "BaseInput",
    props: {
      label: {
        type: String,
        default: "",
      },
      error: {
        type: String,
        default: "",
      },
      modelValue: {
        type: [String, Number],
        required: true,
      },
    },
    setup(props, context) {
      const { updateValue } = SetupFormComponent(props, context);
      const uuid = UniqueId().getId();
      return {
        updateValue,
        uuid,
      };
    },
  };
  </script>
  