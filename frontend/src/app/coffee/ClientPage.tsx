"use client";
import axios from "axios";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";

axios.defaults.baseURL = "http://localhost:8080";

interface Coffee {
  id: number;
  name: string;
  price: number;
  image: string;
}

export default function ClientPage() {
  const [password, setPassword] = useState("");
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [coffees, setCoffees] = useState<Coffee[]>([]);
  const [name, setName] = useState("");
  const [price, setPrice] = useState("");
  const [image, setImage] = useState("");
  const [editingCoffee, setEditingCoffee] = useState<number | null>(null);
  const [modifyPrice, setModifyPrice] = useState("");

  const api = axios.create({
    baseURL: "http://localhost:8080/api",
  });

  useEffect(() => {
    api
      .get("/coffee") // baseURL이 자동으로 붙음
      .then((response) => {
        setCoffees(response.data);
        const initialQuantities: { [key: number]: number } = {};
        response.data.forEach((coffee: Coffee) => {
          initialQuantities[coffee.id] = 0;
        });
      })
      .catch((error) => {
        console.error("Error fetching coffee data:", error);
      });
  }, []);

  const toggleEditing = (coffeeId: number) => {
    if (editingCoffee === coffeeId) {
      setEditingCoffee(null); // 수정 중인 주문을 다시 완료 상태로 변경
      // 수정 완료 시 서버에 업데이트 요청
      updateCoffee(coffeeId); // 업데이트 API 호출
    } else {
      setEditingCoffee(coffeeId); // 해당 주문을 수정 모드로 변경
    }
  };

  const updateCoffee = async (coffeeId: number) => {
    if (window.confirm("정말 커피를 수정하시겠습니까?")) {
      try {
        const updatedCoffeeData = {
          price: modifyPrice,
        };

        // 서버에 PATCH 요청
        const response = await axios.patch(
          `/api/coffee/${coffeeId}`,
          updatedCoffeeData
        );
        console.log("Updated order:", response.data);

        // 상태 업데이트
        setCoffees((prevCoffees) =>
          prevCoffees.map((coffee) =>
            coffee.id === coffeeId ? response.data : coffee
          )
        );
      } catch (error) {
        alert("수정 실패");
      }
    }
  };

  const addCoffee = async () => {
    if (!name || !price || !image) {
      alert("모든 입력란을 채워주세요.");
      return;
    }

    const coffeeData = {
      name: name,
      price: parseInt(price), // 가격은 숫자로 변환
      image: image,
    };

    try {
      const response = await axios.post("/api/coffee/create", coffeeData);
      console.log(response.data); // API 응답 확인
      alert("커피가 추가되었습니다!");
      setCoffees((prevCoffees) => [...prevCoffees, response.data]);
    } catch (error) {
      console.error("커피 추가 실패:", error);
      alert("커피 추가에 실패했습니다.");
    }
  };

  const router = useRouter();
  const handleButtonClick = () => {
    router.push("/");
  };

  const handleDelete = async (coffeeId: number) => {
    if (window.confirm("정말 이 커피를 삭제하시겠습니까?")) {
      try {
        await axios.delete(`/api/coffee/${coffeeId}`);
        setCoffees(coffees.filter((coffee) => coffee.id !== coffeeId)); // 삭제 후 로컬 상태 업데이트
        alert("커피가 삭제되었습니다.");
      } catch (error) {
        alert("삭제 중 오류가 발생했습니다.");
      }
    }
  };

  const formatPrice = (price: number) => {
    return price.toLocaleString();
  };

  useEffect(() => {
    const storedAuth = sessionStorage.getItem("isAuthenticated");
    if (storedAuth === "true") {
      setIsAuthenticated(true);
    }
  }, []);

  const correctPassword = "1234";

  const handleLogin = () => {
    if (password === correctPassword) {
      setIsAuthenticated(true);
      sessionStorage.setItem("isAuthenticated", "true");
    } else {
      alert("비밀번호가 틀렸습니다!");
      setPassword("");
    }
  };

  if (!isAuthenticated) {
    return (
      <div className="flex flex-col items-center justify-center h-screen">
        <h2 className="text-2xl font-bold mb-4">비밀번호를 입력하세요</h2>
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="border border-gray-400 px-4 py-2 rounded-md"
          placeholder="Enter Password"
        />
        <button
          onClick={handleLogin}
          className="mt-4 px-4 py-2 bg-blue-500 text-white rounded-md"
        >
          로그인
        </button>
      </div>
    );
  }

  return (
    <div className="flex flex-col justify-center items-center h-screen">
      <div className="text-4xl font-bold">Grids & Circles</div>

      {/* MENU 컨테이너 */}
      <div className="container flex flex-row w-full h-full px-8 py-5 text-xl font-bold mt-10">
        <div className="flex flex-col w-2/3">
          <div>MENU</div>

          {/* 커피 종류 컨테이너 */}
          <div className="mt-5 flex flex-col space-y-4">
            {coffees.map((coffee, index) => (
              <div
                key={`${coffee.id}-${index}`}
                className="flex items-center justify-between p-4 border border-gray-300 rounded-lg shadow-sm"
              >
                <div className="flex items-center w-full">
                  <img
                    src={coffee.image}
                    alt={coffee.name}
                    className="w-16 h-16 object-cover rounded-md"
                  />
                  <div className="flex flex-col ml-4">
                    <span className="text-lg font-semibold">{coffee.name}</span>
                    <span className="text-gray-600">
                      {editingCoffee === coffee.id ? (
                        <input
                          type="number"
                          value={modifyPrice}
                          onChange={(e) => setModifyPrice(e.target.value)}
                          className="w-[80%] px-4 py-2 border border-gray-300 rounded-md"
                        />
                      ) : (
                        `${formatPrice(coffee.price)} ₩`
                      )}
                    </span>
                  </div>
                  <div className="flex flex-col ml-auto">
                    <button
                      className="bg-blue-400 text-white text-sm px-2 py-1 rounded-md mb-2"
                      onClick={() => {
                        toggleEditing(coffee.id);
                        setPrice(coffee.price.toString()); // 가격 상태 업데이트
                      }}
                    >
                      {editingCoffee === coffee.id ? "Check" : "Edit"}
                    </button>
                    <button
                      onClick={() => handleDelete(coffee.id)}
                      className="bg-red-400 text-white text-sm px-2 py-1 rounded-md"
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* 세로 줄 */}
        <div className="border-l-2 border-gray-300 mx-7"></div>

        {/* INFORM 컨테이너 */}
        <div className="flex flex-col text-lg font-bold w-1/3">
          <div>INFORM</div>
          <div className="mt-5 w-full flex flex-col">
            {/* 커피명 입력란 */}
            <label className="text-base font-semibold mb-2">
              Coffee Name *
            </label>
            <input
              type="text"
              id="name"
              name="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              className="px-4 py-2 border border-gray-300 rounded-md"
            />
          </div>

          {/* 가격 입력란 */}
          <div className="mt-5 w-full flex flex-col">
            <label className="text-base font-semibold mb-2">Price *</label>
            <input
              type="number"
              id="price"
              name="price"
              onChange={(e) => setPrice(e.target.value)}
              className="px-4 py-2 border border-gray-300 rounded-md"
            />
          </div>

          {/* 이미지 입력란 */}
          <div className="mt-5 w-full flex flex-col">
            <label className="text-base font-semibold mb-2">Image URL *</label>
            <input
              type="text"
              id="image"
              name="image"
              value={image}
              onChange={(e) => setImage(e.target.value)}
              className="px-4 py-2 border border-gray-300 rounded-md"
            />
          </div>

          {/* 추가 버튼 */}
          <div className="mt-10">
            <button
              type="button"
              onClick={addCoffee} // 버튼 클릭 시 addCoffee 함수 호출
              className="w-full py-10 bg-custom-red rounded-md hover:bg-custom-red-600"
            >
              Add Coffee
            </button>
          </div>

          {/* 주문 조회 버튼 */}
          <div className="mt-10">
            <button
              type="button"
              onClick={handleButtonClick}
              className="w-full py-4 bg-custom-gray rounded-md hover:bg-custom-red-600"
            >
              메인 메뉴 바로가기
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
