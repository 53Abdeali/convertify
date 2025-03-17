"use client";

import React, { useState } from "react";
import { FaCloudUploadAlt } from "react-icons/fa";

const conversionOptions = [
  { label: "Image Format", value: "image-format" },
  { label: "Image to PDF", value: "image-to-pdf" },
  { label: "Text to PDF", value: "text-to-pdf" },
  { label: "CSV to Excel", value: "csv-to-excel" },
];

export default function Home() {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [conversionType, setConversionType] = useState<string>("image-to-pdf");
  const [targetFormat, setTargetFormat] = useState<string>("png"); // Only used if conversionType is image-format
  const [isConverting, setIsConverting] = useState<boolean>(false);
  const [downloadUrl, setDownloadUrl] = useState<string>("");
  const [message, setMessage] = useState<string>("");

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setSelectedFile(e.target.files[0]);
    }
  };

  const handleConversion = async () => {
    if (!selectedFile) {
      setMessage("Please select a file.");
      return;
    }
    setIsConverting(true);
    setMessage("");
    setDownloadUrl("");

    const formData = new FormData();
    formData.append("file", selectedFile);

    // For image format conversion, include targetFormat
    if (conversionType === "image-format") {
      formData.append("targetFormat", targetFormat);
    }

    // Construct endpoint based on selected conversion type.
    const endpoint = `http://localhost:8080/api/convert/${conversionType}`;

    try {
      const response = await fetch(endpoint, {
        method: "POST",
        body: formData,
      });

      if (response.ok) {
        const data = await response.json();
        setDownloadUrl(data.filePath);
        setMessage(data.message);
      } else {
        const errorData = await response.json();
        setMessage(errorData.error || "Conversion failed.");
      }
    } catch (error) {
      console.error("Conversion failed:", error);
      setMessage("Conversion failed. Please try again.");
    } finally {
      setIsConverting(false);
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100 p-4">
      <h1 className="text-4xl font-bold mb-8">Convertify File Converter</h1>
      <div className="bg-white p-6 rounded shadow-md w-full max-w-md">
        <label className="block mb-4">
          <span className="text-gray-700">Select Conversion Type:</span>
          <select
            value={conversionType}
            onChange={(e) => setConversionType(e.target.value)}
            className="mt-1 block w-full border-gray-300 rounded-md"
          >
            {conversionOptions.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </label>

        {conversionType === "image-format" && (
          <label className="block mb-4">
            <span className="text-gray-700">Target Format:</span>
            <select
              value={targetFormat}
              onChange={(e) => setTargetFormat(e.target.value)}
              className="mt-1 block w-full border-gray-300 rounded-md"
            >
              <option value="png">JPG to PNG</option>
              <option value="jpg">PNG to JPG</option>
            </select>
          </label>
        )}
        <div className="flex items-center justify-center mb-3">
          <label className="w-80 h-48 flex flex-col items-center justify-center border-2 border-dashed border-gray-400 rounded-lg cursor-pointer bg-white shadow-md">
            <FaCloudUploadAlt size={50} className="text-gray-500" />
            <p className="mt-3 text-gray-600">
              {selectedFile
                ? selectedFile.name
                : "Drag & Drop or Click to Upload"}
            </p>
            <input type="file" className="hidden" onChange={handleFileChange} />
          </label>
        </div>

        <button
          onClick={handleConversion}
          disabled={isConverting}
          className="w-full bg-blue-500 text-white py-2 px-4 rounded hover:bg-blue-600 transition-colors"
        >
          {isConverting ? "Converting..." : "Convert File"}
        </button>

        {message && <p className="mt-4 text-center text-gray-700">{message}</p>}

        {downloadUrl && (
          <a
            href={downloadUrl}
            download
            className="mt-4 block text-center bg-green-500 text-white py-2 px-4 rounded hover:bg-green-600 transition-colors"
          >
            Download Converted File
          </a>
        )}
      </div>
    </div>
  );
}
