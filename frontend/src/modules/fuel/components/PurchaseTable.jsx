import { useState } from "react";

const PurchaseTable = ({ purchases = [] }) => {
  const [selectedPurchase, setSelectedPurchase] = useState(null);

  // Empty State
  if (!purchases || purchases.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center py-20 px-4 text-center">
        <div className="bg-gray-50 p-6 rounded-full mb-4">
          <svg className="w-12 h-12 text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
          </svg>
        </div>
        <h3 className="text-lg font-semibold text-gray-800">No Purchase Records</h3>
        <p className="text-gray-500 text-sm mt-1">Record your first entry to see it here.</p>
      </div>
    );
  }

  return (
    <>
      <div className="overflow-x-auto">
        <table className="w-full text-left border-collapse">
          <thead>
            <tr className="bg-gray-50/50 border-b border-gray-100">
              <th className="px-6 py-4 text-[12px] font-bold text-gray-500 uppercase tracking-widest">Date</th>
              <th className="px-6 py-4 text-[12px] font-bold text-gray-500 uppercase tracking-widest">Fuel Type</th>
              <th className="px-6 py-4 text-[12px] font-bold text-gray-500 uppercase tracking-widest text-right">Quantity</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-50 bg-white">
            {purchases.map((purchase) => (
              <tr 
                key={purchase.purchaseId} 
                className="hover:bg-blue-50/30 transition-colors cursor-pointer group"
                onClick={() => setSelectedPurchase(purchase)}
              >
                <td className="px-6 py-4 whitespace-nowrap text-[13px] font-medium text-gray-600">
                  {purchase.purchaseDate ? new Date(purchase.purchaseDate).toLocaleDateString('en-IN', {
                    day: '2-digit',
                    month: 'short',
                    year: 'numeric'
                  }) : 'N/A'}
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <span className={`inline-flex items-center px-2.5 py-1 rounded-md text-[11px] font-bold uppercase tracking-wide ${
                    purchase.fuelType === 'PETROL' 
                      ? 'bg-orange-50 text-orange-600 border border-orange-100' 
                      : 'bg-blue-50 text-blue-600 border border-blue-100'
                  }`}>
                    {purchase.fuelType}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-right text-[14px] text-gray-800 font-semibold group-hover:text-blue-600 transition-colors">
                  {parseFloat(purchase.quantity || 0).toLocaleString()} L
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Details Modal */}
      {selectedPurchase && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/40 backdrop-blur-sm transition-opacity">
          {/* Modal Overlay / Backdrop Click Handler */}
          <div 
            className="absolute inset-0" 
            onClick={() => setSelectedPurchase(null)}
          ></div>
          
          {/* Modal Card */}
          <div className="relative bg-white rounded-2xl shadow-2xl w-full max-w-md overflow-hidden transform transition-all">
            {/* Header */}
            <div className={`px-6 py-5 border-b border-gray-100 ${
              selectedPurchase.fuelType === 'PETROL' ? 'bg-orange-50/50' : 'bg-blue-50/50'
            }`}>
              <div className="flex justify-between items-start">
                <div>
                  <h3 className="text-xl font-bold text-gray-900">Purchase Details</h3>
                  <p className="text-sm text-gray-500 font-mono mt-1 text-[11px]">ID: #{selectedPurchase.purchaseId}</p>
                </div>
                <button 
                  onClick={() => setSelectedPurchase(null)}
                  className="text-gray-400 hover:text-gray-600 transition-colors p-1 rounded-full hover:bg-gray-100"
                >
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </div>
            </div>

            {/* Body */}
            <div className="px-6 py-6 space-y-5">
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-1">
                  <p className="text-[11px] font-bold text-gray-400 uppercase tracking-widest">Date</p>
                  <p className="text-sm font-semibold text-gray-800">
                    {selectedPurchase.purchaseDate ? new Date(selectedPurchase.purchaseDate).toLocaleDateString('en-IN', {
                      day: '2-digit', month: 'short', year: 'numeric'
                    }) : 'N/A'}
                  </p>
                </div>
                <div className="space-y-1">
                  <p className="text-[11px] font-bold text-gray-400 uppercase tracking-widest">Fuel Type</p>
                  <span className={`inline-flex px-2 py-0.5 rounded text-[11px] font-bold uppercase tracking-tight ${
                    selectedPurchase.fuelType === 'PETROL' 
                      ? 'bg-orange-100 text-orange-700' 
                      : 'bg-blue-100 text-blue-700'
                  }`}>
                    {selectedPurchase.fuelType}
                  </span>
                </div>
              </div>

              <div className="space-y-1">
                <p className="text-[11px] font-bold text-gray-400 uppercase tracking-widest">Vendor Name</p>
                <div className="flex items-center text-sm font-semibold text-gray-800">
                  <svg className="w-4 h-4 mr-2 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4" />
                  </svg>
                  {selectedPurchase.vendorName}
                </div>
              </div>

              <div className="bg-gray-50 rounded-xl p-4 space-y-3">
                <div className="flex justify-between items-center text-sm">
                  <span className="font-medium text-gray-600">Quantity</span>
                  <span className="font-bold text-gray-900">{parseFloat(selectedPurchase.quantity || 0).toLocaleString()} Liters</span>
                </div>
                <div className="flex justify-between items-center text-sm">
                  <span className="font-medium text-gray-600">Price / Liter</span>
                  <span className="font-mono text-gray-700">₹{parseFloat(selectedPurchase.pricePerLiter || 0).toFixed(2)}</span>
                </div>
                <div className="pt-3 border-t border-gray-200 flex justify-between items-center">
                  <span className="font-bold text-gray-800">Total Cost</span>
                  <span className="text-lg font-extrabold text-blue-600">
                    ₹{parseFloat(selectedPurchase.totalCost || 0).toLocaleString(undefined, { minimumFractionDigits: 2 })}
                  </span>
                </div>
              </div>
            </div>

            {/* Footer */}
            <div className="px-6 py-4 bg-gray-50 border-t border-gray-100 flex justify-end">
              <button 
                onClick={() => setSelectedPurchase(null)}
                className="px-5 py-2 bg-white border border-gray-300 rounded-lg text-sm font-semibold text-gray-700 hover:bg-gray-50 hover:text-gray-900 transition-colors shadow-sm focus:outline-none focus:ring-2 focus:ring-gray-200"
              >
                Close
              </button>
            </div>
          </div>
        </div>
      )}
    </>
  );
};

export default PurchaseTable;