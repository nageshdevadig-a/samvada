import SignupForm from "../components/loginHome/SignupForm";


const CreateAccount = ({ onAccountCreated }) => {
    return (
       <div className="flex flex-col min-h-screen bg-black text-white font-sans">
            <SignupForm onAccountCreated={onAccountCreated} />
        </div>
    )

}

export default CreateAccount;