const InventoryTable = ({ inventory }) => {

  return (

    <div className="overflow-x-auto bg-white rounded-xl shadow">

      <table className="min-w-full">

        <thead className="bg-gray-100">

          <tr>

            <th className="px-6 py-3 text-left">
              Fuel Type
            </th>

            <th className="px-6 py-3 text-left">
              Available Liters
            </th>

            <th className="px-6 py-3 text-left">
              Source
            </th>

          </tr>

        </thead>



        <tbody>

          {
            inventory.map((item, index) => (

              <tr
                key={index}
                className="border-b hover:bg-gray-50"
              >

                {/* Fuel Type */}
                <td className="px-6 py-4 font-semibold">
                  {item.fuelType}
                </td>



                {/* Available Liters */}
                <td className="px-6 py-4">
                  {item.availableLiters} L
                </td>



                {/* Source */}
                <td className="px-6 py-4">
                  {item.source}
                </td>

              </tr>
            ))
          }

        </tbody>

      </table>

    </div>
  );
};

export default InventoryTable;