import styled from 'styled-components';
import Avartar from '../atoms/Avartar';
import { Link } from 'react-router-dom';
import ReactMarkdown from 'react-markdown';

const QuestionSummaryContent = ({
  question_id,
  color,
  title,
  content,
  displayname,
  createdAt,
}) => {
  return (
    <QuestionSummaryContenLayout>
      <h3>
        <Title>
          <Link to="/QuestionDetailPage" state={{ question_id: question_id }}>
            {title}
          </Link>
        </Title>
      </h3>
      <QuestionContent>
        <ReactMarkdown>{content.slice(0, 120)}</ReactMarkdown>....
      </QuestionContent>
      <UserBox>
        <Avartar color={color} />
        <Username>{displayname}</Username>
        <span>{createdAt}</span>
      </UserBox>
    </QuestionSummaryContenLayout>
  );
};

export default QuestionSummaryContent;

const QuestionSummaryContenLayout = styled.div`
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  max-width: 100%;

  h3 {
    vertical-align: baseline;
    font-weight: 400;
  }
`;

const Title = styled.div`
  width: 595px;
  height: 34px;
  a {
    color: var(--theme-post-title-color);
    word-break: break-word;
    overflow-wrap: break-word;
  }
`;

const QuestionContent = styled.div`
  display: flex;
  color: var(--fc-medium);
  font-size: 13px;
  flex-wrap: wrap;

  width: 595px;
  height: 34px;
`;

const UserBox = styled.div`
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-top: 20px;
  margin-left: auto;
  flex-wrap: wrap;

  span {
    color: var(--black-500);
    font-size: 12px;
  }
`;

const Username = styled.a`
  color: var(--theme-link-color-hover);
  font-size: 12px;
  margin: 0 5px;
`;
