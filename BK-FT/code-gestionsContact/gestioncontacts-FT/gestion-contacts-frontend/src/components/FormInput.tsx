interface Props {
  name: string;
  placeholder: string;
  value: string;
  error?: string;
  touched?: boolean;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onBlur: (e: React.FocusEvent<HTMLInputElement>) => void;
}

const FormInput = ({
  name,
  placeholder,
  value,
  error,
  touched,
  onChange,
  onBlur,
}: Props) => {
  const borderColor = !touched
    ? "border-gray-300"
    : error
    ? "border-red-500"
    : "border-green-500";

  return (
    <div className="mb-4">
      <input
        name={name}
        value={value}
        onChange={onChange}
        onBlur={onBlur}
        placeholder={placeholder}
        className={`w-full p-2 border rounded ${borderColor} focus:outline-none`}
      />
      {touched && error && (
        <p className="text-red-500 text-sm mt-1">{error}</p>
      )}
    </div>
  );
};

export default FormInput;
