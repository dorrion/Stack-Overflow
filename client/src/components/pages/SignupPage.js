import styled from 'styled-components';
import Signupinfo from '../organism/SignupInfo';
import Signupsubmit from '../organism/SignupSubmit';

//회원가입 페이지

const SignupPageForm = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  @media screen and (max-width: 815px) {
    flex-direction: column;
  }
  background-color: var(--black-050);
  padding: 40px 0 60px 0;
`;

const SignupPage = () => {
  document.title = 'Signup Page';
  return (
    <SignupPageForm>
      <Signupinfo></Signupinfo>
      <Signupsubmit></Signupsubmit>
    </SignupPageForm>
  );
};

export default SignupPage;
