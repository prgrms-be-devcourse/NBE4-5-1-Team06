"use client";

import { SetStateAction, useEffect, useState } from "react";
import axios from "axios";
import { useRouter } from "next/navigation";

axios.defaults.baseURL = "http://localhost:8080/api";

interface Coffee {
  id: number;
  name: string;
  price: number;
  image: string;
}

export default function ClientPage() {
  const router = useRouter();
  const [coffees, setCoffees] = useState<Coffee[]>([]);
  const [quantities, setQuantities] = useState<{ [key: number]: number }>({});
  const [email, setEmail] = useState("");
  const [address, setAddress] = useState("");
  const [totalPrice, setTotalPrice] = useState(0);
  const [orderCoffees, setOrderCoffees] = useState<
    { id: number; quantity: number }[]
  >([]);

  const api = axios.create({
    baseURL: "http://localhost:8080/api",
  });

  // 커피 목록을 가져오는 useEffect
  useEffect(() => {
    api
      .get("/coffee")
      .then((response) => {
        setCoffees(response.data);
        const initialQuantities: { [key: number]: number } = {};
        response.data.forEach((coffee: Coffee) => {
          initialQuantities[coffee.id] = 0;
        });
        setQuantities(initialQuantities);
      })
      .catch((error) => {
        console.error("Error fetching coffee data:", error);
      });
  }, []);

  // 주문 페이지로 이동
  const handleButtonClick = () => {
    router.push("/order");
  };

  const handleButtonClick2 = () => {
    router.push("/coffee");
  };

  // 커피 수량 변경 처리
  const handleQuantityChange = (id: number, delta: number) => {
    setQuantities((prev) => {
      const updatedQuantities = {
        ...prev,
        [id]: Math.max(0, prev[id] + delta),
      };
      updateTotalPrice(updatedQuantities); // 수량 변경 시 가격 업데이트
      return updatedQuantities;
    });
  };

  // 가격 포맷 함수
  const formatPrice = (price: number) => {
    return price.toLocaleString();
  };

  // 총 가격 계산 및 주문할 커피 목록 업데이트
  const updateTotalPrice = (updatedQuantities: { [key: number]: number }) => {
    let total = 0;
    const updatedOrderCoffees: SetStateAction<
      { id: number; quantity: number }[]
    > = [];

    Object.entries(updatedQuantities).forEach(([id, quantity]) => {
      const coffee = coffees.find((c) => c.id === Number(id));
      if (coffee && quantity > 0) {
        total += coffee.price * quantity;
        updatedOrderCoffees.push({ id: coffee.id, quantity });
      }
    });

    setTotalPrice(total);
    setOrderCoffees(updatedOrderCoffees); // 주문할 커피 목록 업데이트
  };

  // 주문 처리
  const handleOrderButtonClick = async () => {
    const orderRequestDto = {
      email: email,
      address: address,
      totalPrice: totalPrice,
      orderCoffees: orderCoffees.map((coffee) => ({
        coffeeId: coffee.id,
        quantity: coffee.quantity,
      })),
    };

    const userConfirmed = window.confirm("주문하시겠습니까?");

    if (userConfirmed) {
      try {
        await axios.post("/order/create", orderRequestDto);
        alert(
          "주문이 완료되었습니다. 당일 오후 2시 이후의 주문은 다음날 배송을 시작합니다."
        );
        window.location.reload();
      } catch (error) {
        console.error("주문 생성 실패:", error);
      }
    }
  };

  return (
    <div className="flex flex-col justify-center items-center h-screen">
      <div className="text-4xl font-bold">Grids & Circles</div>

      {/* MENU 컨테이너 */}
      <div className="container flex flex-row w-full h-full px-8 py-5 text-xl font-bold mt-10">
        <div className="flex flex-col w-2/3">
          <div>MENU</div>

          {/* 커피 종류 컨테이너 */}
          <div className="mt-5 flex flex-col space-y-4">
            {coffees.length > 0 ? (
              coffees.map((coffee) => (
                <div
                  key={coffee.id}
                  className="flex items-center justify-between p-4 border border-gray-300 rounded-lg shadow-sm"
                >
                  <div className="flex items-center space-x-4">
                    <img
                      src={coffee.image}
                      alt={coffee.name}
                      className="w-16 h-16 object-cover rounded-md"
                    />
                    <div className="flex flex-col">
                      <span className="text-lg font-semibold">
                        {coffee.name}
                      </span>
                      <span className="text-gray-600">
                        {formatPrice(coffee.price)} ₩
                      </span>
                    </div>
                  </div>
                  <div className="flex items-center space-x-2">
                    <button
                      onClick={() => handleQuantityChange(coffee.id, -1)}
                      className="px-2 py-1 bg-gray-200 rounded"
                    >
                      -
                    </button>
                    <span className="text-lg font-semibold">
                      {quantities[coffee.id]}
                    </span>
                    <button
                      onClick={() => handleQuantityChange(coffee.id, 1)}
                      className="px-2 py-1 bg-gray-200 rounded"
                    >
                      +
                    </button>
                  </div>
                </div>
              ))
            ) : (
              <div className="text-gray-500">Loading...</div>
            )}
          </div>

          {/* 커피 수량 및 총 가격*/}
          <div className="mr-10 flex flex-col text-sm font-bold w-full text-right justify-end">
            <div className="mt-5">
              {Object.entries(quantities)
                .filter(([_, quantity]) => quantity > 0)
                .map(([id, quantity]) => {
                  const coffee = coffees.find((c) => c.id === Number(id));
                  return coffee ? (
                    <div key={id} className="flex justify-end space-x-2">
                      <span>
                        {coffee.name} x {quantity}
                      </span>
                    </div>
                  ) : null;
                })}
            </div>
            <div className="mt-5 text-xl font-bold">
              Total Price: {formatPrice(totalPrice)} ₩
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
              value={email}
              onChange={(e) => setEmail(e.target.value)}
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
              value={address}
              onChange={(e) => setAddress(e.target.value)}
              rows={5}
              className="px-4 py-2 border border-gray-300 rounded-md"
            />
          </div>

          {/* ORDER 버튼 */}
          <div className="mt-10">
            <button
              type="button"
              className="w-full py-10 bg-custom-red rounded-md hover:bg-custom-red-600"
              onClick={handleOrderButtonClick}
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

          {/* 메뉴 관리 버튼 */}
          <div className="mt-10">
            <button
              onClick={handleButtonClick2}
              type="button"
              className="w-full py-4 bg-custom-gray rounded-md hover:bg-custom-red-600"
            >
              메뉴 관리
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
