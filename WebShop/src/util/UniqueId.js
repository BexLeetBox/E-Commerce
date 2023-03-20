let UUID = 0;

export default function UniqueId() {
  const getId = () => {
    UUID++;
    return UUID;
  };
  return { getId };
}
