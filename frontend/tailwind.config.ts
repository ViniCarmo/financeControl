import type { Config } from "tailwindcss";

const config: Config = {
  content: ["./index.html", "./src/**/*.{ts,tsx}"],
  theme: {
    extend: {
      colors: {
        income: "#16a34a",
        expense: "#dc2626"
      }
    }
  },
  plugins: []
};

export default config;
