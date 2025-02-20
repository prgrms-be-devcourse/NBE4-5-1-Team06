"use client";

import { useRouter } from "next/navigation";

export default function ClientPage() {
  const router = useRouter();
  const handleButtonClick = () => {
    router.push("/order");
  };

  return (
    <div className="flex flex-col justify-center items-center h-screen">
      <div className="text-4xl font-bold">Grids & Circles</div>

      {/* MENU 컨테이너 */}
      <div className="container flex flex-row w-full h-full px-8 py-5 text-xl font-bold mt-10">
        <div className="flex flex-col w-2/3">
          <div>MENU</div>

          {/* 커피 종류 컨테이너 */}
          <div className="mt-5 flex flex-col ">
            <div className="container-coffee max-w-full h-20 mt-4 justify-around text-lg font-medium">
              Coffee 1
            </div>
          </div>
        </div>

        {/* 세로 줄 */}
        <div className="border-l-2 border-gray-300 mx-7"></div>

        {/* INFORM 컨테이너 */}
        <div className="flex flex-col text-lg font-bold w-1/3">
          <div>INFORM</div>
          <div className="mt-5 w-full flex flex-col">
            {/* Email 입력란 */}
            <label htmlFor="email" className="text-base font-semibold mb-2">
              Email *
            </label>
            <input
              type="email"
              id="email"
              name="email"
              className="px-4 py-2 border border-gray-300 rounded-md"
            />
          </div>

          {/* Address 입력란 */}
          <div className="mt-5 w-full flex flex-col">
            <label htmlFor="address" className="text-base font-semibold mb-2">
              Address *
            </label>
            <textarea
              id="address"
              name="address"
              rows={5}
              className="px-4 py-2 border border-gray-300 rounded-md"
            />
          </div>

          {/* ORDER 버튼 */}
          <div className="mt-10">
            <button
              type="button"
              className="w-full py-10 bg-custom-red rounded-md hover:bg-custom-red-600"
            >
              ORDER
            </button>
          </div>

          {/* 주문 조회 버튼 */}
          <div className="mt-10">
            <button
              onClick={handleButtonClick}
              type="button"
              className="w-full py-4 bg-custom-gray rounded-md hover:bg-custom-red-600"
            >
              주문 조회 바로가기
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
