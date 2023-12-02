import React, { createContext, useContext, useCallback } from 'react';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

const ToastContext = createContext({
  showToast: (message, options) => {},
});

export const ToastProvider = ({ children }) => {
  const showToast = useCallback((message, options) => {
    toast(message, options);
  }, []);

  return (
      <ToastContext.Provider value={{ showToast }}>
        {children}
        <ToastContainer />
      </ToastContext.Provider>
  );
};

export const useToast = () => useContext(ToastContext);