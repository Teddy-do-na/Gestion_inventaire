import { useEffect, useState, FormEvent } from "react";
import axios from "axios";
import { Contact } from "../types/Contact";
import FormInput from "./FormInput";
import { useContactValidation, Errors } from "../hooks/useContactValidation";

type Touched = Partial<Record<keyof Contact, boolean>>;

const API_URL = "http://localhost:8080/contact";

const ContactForm = () => {
  const [contact, setContact] = useState<Contact>({
    nom: "",
    prenom: "",
    telephone: "",
    email: "",
  });

  const [errors, setErrors] = useState<Errors>({});
  const [touched, setTouched] = useState<Touched>({});

  const { validate } = useContactValidation(contact);

  useEffect(() => {
    setErrors(validate());
  }, [contact]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setContact({ ...contact, [e.target.name]: e.target.value });
  };

  const handleBlur = (e: React.FocusEvent<HTMLInputElement>) => {
    setTouched({ ...touched, [e.target.name]: true });
  };

  const handleSubmit = async (e: FormEvent) => {
  e.preventDefault();

 
  const validationErrors = validate();
  setErrors(validationErrors);
  setTouched({
    nom: true,
    prenom: true,
    telephone: true,
    email: true,
  });

  if (Object.keys(validationErrors).length > 0) return;

  try {
    
    const response = await axios.post(API_URL, contact);
    // console.log("Contact ajouté avec succès :", response.data);

    alert("Contact ajouté avec succès");

   
    setContact({ nom: "", prenom: "", telephone: "", email: "" });
    setTouched({});
    setErrors({});
  } catch (error: any) {
    
    if (error.response) {
      
      // console.error("Erreur backend :", error.response.data);
      alert(
        "Erreur lors de l'ajout : " +
          (typeof error.response.data === "string"
            ? error.response.data
            : JSON.stringify(error.response.data))
      );
    } else if (error.request) {
      
      // console.error("Pas de réponse du serveur :", error.request);
      alert("Impossible de contacter le serveur. Vérifiez qu'il est lancé.");
    } else {
     
      // console.error("Erreur Axios :", error.message);
      alert("Une erreur est survenue : " + error.message);
    }
  }
};


  return (
    <form
      onSubmit={handleSubmit}
      className="flex flex-col justify-center  max-w-md mx-auto mt-10 p-6 bg-red-100 shadow rounded"
    >
      <FormInput
        name="nom"
        placeholder="Nom"
        value={contact.nom}
        onChange={handleChange}
        onBlur={handleBlur}
        error={errors.nom}
        touched={touched.nom}
      />

      <FormInput
        name="prenom"
        placeholder="Prénom"
        value={contact.prenom}
        onChange={handleChange}
        onBlur={handleBlur}
        error={errors.prenom}
        touched={touched.prenom}
      />

      <FormInput
        name="telephone"
        placeholder="Téléphone"
        value={contact.telephone}
        onChange={handleChange}
        onBlur={handleBlur}
        error={errors.telephone}
        touched={touched.telephone}
      />

      <FormInput
        name="email"
        placeholder="Email"
        value={contact.email}
        onChange={handleChange}
        onBlur={handleBlur}
        error={errors.email}
        touched={touched.email}
      />

      <button
        type="submit"
        className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
      >
        Ajouter
      </button>
    </form>
  );
};

export default ContactForm;
