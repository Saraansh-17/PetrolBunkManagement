import { useState } from "react";

import { useNavigate } from "react-router-dom";

import {
  loginUser,
  getCurrentUser,
} from "../services/authService";

import { useAuth } from "../context/useAuth";


const Login = () => {

  const navigate = useNavigate();

  const { setUser } = useAuth();


  const [formData, setFormData] = useState({
    employeeId: "",
    password: "",
  });

  const [loading, setLoading] = useState(false);


  // ==============================
  // Handle Input Change
  // ==============================

  const handleChange = (e) => {

    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };


  // ==============================
  // Handle Login
  // ==============================

  const handleSubmit = async (e) => {

    e.preventDefault();

    try {

      setLoading(true);

      // Login Request
      await loginUser(formData);

      // Fetch Current User
      const userData = await getCurrentUser();

      // Save User In Context
      setUser(userData);

      // Redirect
      navigate("/");

    } catch (error) {

      console.log(error);

      alert("Invalid Credentials");

    } finally {

      setLoading(false);
    }
  };


  return (
    <div className="flex items-center justify-center min-h-[80vh]">

      <form
        onSubmit={handleSubmit}
        className="bg-white p-8 rounded-xl shadow-lg w-full max-w-md space-y-6"
      >

        <h1 className="text-3xl font-bold text-center">
          Login
        </h1>

        {/* Employee ID */}
        <input
          type="text"
          name="employeeId"
          placeholder="Employee ID"
          value={formData.employeeId}
          onChange={handleChange}
          className="w-full border p-3 rounded-lg"
          required
        />

        {/* Password */}
        <input
          type="password"
          name="password"
          placeholder="Password"
          value={formData.password}
          onChange={handleChange}
          className="w-full border p-3 rounded-lg"
          required
        />

        {/* Submit Button */}
        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700"
        >
          {
            loading
              ? "Logging In..."
              : "Login"
          }
        </button>

      </form>

    </div>
  );
};

export default Login;