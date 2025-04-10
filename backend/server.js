const express = require("express");
const Stripe = require("stripe");
const cors = require("cors");

const app = express();
const stripe = Stripe("sk_test_51RBz62E0IzDCw21lINcI1kpT8to7JVofDr9K5itvEPNptaDMHBUUn9pedvqgr50tnKnd4zi4nAE7d82sCrHoJ4V500J39CYt11"); // Replace with your secret key

app.use(cors());
app.use(express.json());

app.post("/create-payment-intent", async (req, res) => {
  const { amount } = req.body;

  try {
    const paymentIntent = await stripe.paymentIntents.create({
      amount: amount, // amount in cents (e.g., $10 = 1000)
      currency: "aud",
      payment_method_types: ["card"],
    });

    res.send({
      clientSecret: paymentIntent.client_secret,
    });
  } catch (e) {
    res.status(500).send({ error: e.message });
  }
});

app.listen(4242, () => console.log("Server running on http://localhost:4242"));
