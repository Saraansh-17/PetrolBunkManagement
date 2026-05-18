import { useState } from "react";
import { recordFuelPurchase } from "../services/fuelService";
import { useAuth } from "../../Auth/context/useAuth";

const PurchaseForm = ({ fetchPurchases }) => {
  const { user } = useAuth();

  const [formData, setFormData] = useState({
    vendorName: "",
    fuelType: "",
    quantity: 0,
    pricePerLiter: 0,
  });

  const [loading, setLoading] = useState(false);

  // Derived Total Cost
  const totalCost = (
    (parseFloat(formData.quantity) || 0) *
    (parseFloat(formData.pricePerLiter) || 0)
  ).toFixed(2);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      const payload = {
        ...formData,
        employeeId: user?.employeeId,
        totalCost: Number(totalCost),
        quantity: Number(formData.quantity),
        pricePerLiter: Number(formData.pricePerLiter),
      };
      console.log(payload);
      await recordFuelPurchase(payload);
      await fetchPurchases();
      setFormData({
        vendorName: "",
        fuelType: "",
        quantity: "",
        pricePerLiter: "",
      });
    } catch (error) {
      console.error("Failed to add purchase:", error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white p-6 rounded-xl border border-gray-200 shadow-sm">
      <div className="mb-6">
        <h2 className="text-xl font-bold text-gray-800 tracking-tight">Record New Supply</h2>
        <p className="text-sm text-gray-500">Fill in the details from the vendor invoice</p>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          {/* Vendor Name */}
          <div className="md:col-span-2 space-y-1.5">
            <label className="text-[13px] font-bold text-gray-600 uppercase tracking-wider ml-1">
              Vendor Name
            </label>
            <input
              type="text"
              name="vendorName"
              placeholder="e.g. Bharat Petroleum"
              className="w-full px-4 py-2.5 rounded-lg border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition-all outline-none text-sm"
              value={formData.vendorName}
              onChange={handleChange}
              required
            />
          </div>

          {/* Fuel Type */}
          <div className="space-y-1.5">
            <label className="text-[13px] font-bold text-gray-600 uppercase tracking-wider ml-1">
              Fuel Type
            </label>
            <select
              name="fuelType"
              className="w-full px-4 py-2.5 rounded-lg border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition-all outline-none text-sm appearance-none bg-no-repeat bg-position-[right_1rem_center] bg-size-[1em_1em]"
              style={{ backgroundImage: `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 24 24' stroke='currentColor'%3E%3Cpath stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M19 9l-7 7-7-7'%3E%3C/path%3E%3C/svg%3E")` }}
              value={formData.fuelType}
              onChange={handleChange}
              required
            >
              <option value="">Select Type</option>
              <option value="PETROL">Petrol</option>
              <option value="DIESEL">Diesel</option>
            </select>
          </div>

          {/* Quantity */}
          <div className="space-y-1.5">
            <label className="text-[13px] font-bold text-gray-600 uppercase tracking-wider ml-1">
              Quantity (L)
            </label>
            <input
              type="number"
              name="quantity"
              placeholder="0.00"
              step="0.01"
              className="w-full px-4 py-2.5 rounded-lg border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition-all outline-none text-sm"
              value={formData.quantity}
              onChange={handleChange}
              required
            />
          </div>

          {/* Price Per Liter */}
          <div className="space-y-1.5">
            <label className="text-[13px] font-bold text-gray-600 uppercase tracking-wider ml-1">
              Price / Liter
            </label>
            <input
              type="number"
              name="pricePerLiter"
              placeholder="0.00"
              step="0.01"
              className="w-full px-4 py-2.5 rounded-lg border border-gray-200 focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition-all outline-none text-sm"
              value={formData.pricePerLiter}
              onChange={handleChange}
              required
            />
          </div>

          {/* Total Cost */}
          <div className="md:col-span-2 space-y-1.5">
            <label className="text-[13px] font-bold text-gray-600 uppercase tracking-wider ml-1">
              Total Payable (Calculated)
            </label>
            <div className="relative">
              <span className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 text-sm font-semibold">₹</span>
              <input
                type="text"
                className="w-full pl-8 pr-4 py-2.5 rounded-lg border border-gray-200 bg-gray-50 font-bold text-blue-600 outline-none text-sm"
                value={totalCost}
                readOnly
              />
            </div>
          </div>

          {/* Submit Button */}
          <div className="md:col-span-1 flex items-end">
            <button
              type="submit"
              disabled={loading}
              className="w-full py-2.5 rounded-lg bg-blue-600 text-white font-semibold text-sm hover:bg-blue-700 active:bg-blue-800 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors shadow-sm"
            >
              {loading ? "Saving..." : "Record Entry"}
            </button>
          </div>
        </div>
      </form>
    </div>
  );
};

export default PurchaseForm;