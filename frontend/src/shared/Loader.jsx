
/*
 * Loader - a full‑screen overlay component.
 * Props:
 *   message (string) – optional custom loading text (default: "Loading...")
 *   className (string) – optional additional classes for the outer wrapper.
 */
const Loader = ({ message = "Loading...", className = "" }) => {
  return (
    <div
      className={`fixed inset-0 bg-black bg-opacity-30 flex items-center justify-center z-50 ${className}`}
    >
      {/* Spinner */}
      <div className="flex flex-col items-center space-y-4">
        <div className="w-12 h-12 border-4 border-gray-200 border-t-blue-600 rounded-full animate-spin" />
        <p className="text-gray-700 font-medium animate-pulse">{message}</p>
      </div>
    </div>
  );
};

export default Loader;

 