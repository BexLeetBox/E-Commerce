/**
 * Represents a setup form component that handles updating the model value
 *
 * @param {Object} props - The props object that contains the component properties
 * @param {Function} emit - The emit function to handle emitting events
 * @returns {Object} An object containing the `updateValue` function
 */
export default function SetupFormComponent(props, { emit }) {
  /**
   * Updates the value of the model based on the input event
   *
   * @param {Object} event - The event object that contains the target input value
   */
  const updateValue = (event) => {
    let val = event.target.value;

    if (event.target.type === "checkbox") {
      val = event.target.checked;
    }
    
    if (event.target.type === "radio") {
      val = props.value;
    }

    emit("update:modelValue", val);
  };

  return { updateValue };
}
