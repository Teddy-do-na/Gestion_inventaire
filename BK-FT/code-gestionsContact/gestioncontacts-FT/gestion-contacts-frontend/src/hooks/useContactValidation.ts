import { Contact } from "../types/Contact";

export type Errors = Partial<Record<keyof Contact, string>>;

export const useContactValidation = (contact: Contact) => {
  const validate = (): Errors => {
    const errors: Errors = {};

    if (!contact.nom.trim()) {
      errors.nom = "Nom obligatoire";
    }

    if (!contact.prenom.trim()) {
      errors.prenom = "Prénom obligatoire";
    }

    if (!/^\+\d{7,15}$/.test(contact.telephone)) {
      errors.telephone = "Téléphone international invalide (ex: +237 693726234)";
    }


    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(contact.email)) {
      errors.email = "Email invalide";
    }

    return errors;
  };

  return { validate };
};
