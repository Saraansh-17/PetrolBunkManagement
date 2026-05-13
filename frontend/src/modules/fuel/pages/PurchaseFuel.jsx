import { useEffect, useState } from "react";
import PurchaseForm from "../components/PurchaseForm";
import PurchaseTable from "../components/PurchaseTable";
import { getAllPurchases } from "../services/fuelService";
import Loader from "../../../shared/Loader";

const PurchaseFuel = () => {
  const [purchases, setPurchases] = useState([]);
  const [loading, setLoading] = useState(true);

  // Fetch Purchases
  const fetchPurchases = async () => {
    try {
      setLoading(true);
      const data = await getAllPurchases();
      setPurchases(data.content || []);
    } catch (error) {
      console.error("Error fetching purchases:", error);
    } finally {
      setLoading(false);
    }
  };

  // Initial Fetch
  useEffect(() => {
    const loadPurchases = async () => {
      await fetchPurchases();
    };
    loadPurchases();
  }, []);

  return (
    <div className="min-h-screen bg-gray-50 pb-12">
      {/* Header Section */}
      <div className="bg-white border-b border-gray-200 shadow-sm">
        <div className="max-w-6xl mx-auto px-4 py-8">
          <div className="flex items-center justify-between">
            <div>
              <h1 className="text-2xl font-bold text-gray-900">Fuel Purchase Log</h1>
              <p className="text-sm text-gray-500 mt-1">Manage and track your fuel supply inventory</p>
            </div>
            <button 
              onClick={fetchPurchases}
              className="flex items-center px-4 py-2 text-sm font-medium text-gray-600 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors"
            >
              <svg className="w-4 h-4 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
              </svg>
              Refresh
            </button>
          </div>
        </div>
      </div>

      {/* Main Content */}
      <main className="max-w-6xl mx-auto px-4 mt-8 space-y-8">
        {/* Form Section */}
        <section>
          <PurchaseForm fetchPurchases={fetchPurchases} />
        </section>

        {/* Table Section */}
        <section className="bg-white rounded-xl border border-gray-200 overflow-hidden shadow-sm">
          <div className="px-6 py-4 border-b border-gray-200 bg-white flex items-center justify-between">
            <h3 className="font-semibold text-gray-800">Purchase History</h3>
            <span className="text-xs font-medium text-gray-500 bg-gray-100 px-2.5 py-1 rounded-full">
              {purchases.length} Entries
            </span>
          </div>
          
          <div className="relative min-h-[300px]">
            {loading ? (
              <div className="absolute inset-0 flex justify-center items-center bg-white/60 backdrop-blur-[2px] z-10">
                <Loader message="Loading records..." />
              </div>
            ) : null}
            <PurchaseTable purchases={purchases} />
          </div>
        </section>
      </main>
    </div>
  );
};

export default PurchaseFuel;
