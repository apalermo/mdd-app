import { defineConfig } from "vite";
import angular from "@analogjs/vite-plugin-angular";

export default defineConfig(({ mode }) => ({
  plugins: [angular()],
  test: {
    globals: true,
    environment: "jsdom",
    setupFiles: ["src/test-setup.ts"],
    include: ["src/**/*.{test,spec}.{js,mjs,cjs,ts,mts,cts,jsx,tsx}"],
    reporters: ["default"],
  },

  coverage: {
    provider: "v8",
    reporter: ["text", "json", "html"],
    reportsDirectory: "./coverage",
    exclude: [
      "src/main.ts",
      "src/test-setup.ts",
      "src/app/app.config.ts",
      "src/app/app.routes.ts",
      "**/*.interface.ts",
      "**/*.spec.ts",
      "src/app/models/**",
      "coverage/**",
      "**/*.d.ts",
    ],
  },
  define: {
    "import.meta.vitest": mode !== "production",
  },
}));
