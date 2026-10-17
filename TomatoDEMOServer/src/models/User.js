import mongoose from "mongoose";

const userSchema = new mongoose.Schema({
  phone: {
    type: String,
    required: true,
    unique: true,
    trim: true
  },
  name: {
    type: String,
    default: "Foodie User"
  },
  email: {
    type: String,
    default: ""
  },
  otp: {
    type: String,
    default: null
  },
  otpExpiresAt: {
    type: Date,
    default: null
  },
  addresses: [{
    label: String,
    houseNo: String,
    areaOrVillage: String,
    cityOrDistrict: String,
    state: String,
    pinCode: String,
    isDefault: Boolean
  }]
}, {
  timestamps: true
});

export const User = mongoose.model("User", userSchema);
