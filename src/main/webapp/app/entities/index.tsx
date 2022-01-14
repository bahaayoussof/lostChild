import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Child from './child';
import Description from './description';
import Address from './address';
import LastSeen from './last-seen';
import ContactInformation from './contact-information';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}child`} component={Child} />
      <ErrorBoundaryRoute path={`${match.url}description`} component={Description} />
      <ErrorBoundaryRoute path={`${match.url}address`} component={Address} />
      <ErrorBoundaryRoute path={`${match.url}last-seen`} component={LastSeen} />
      <ErrorBoundaryRoute path={`${match.url}contact-information`} component={ContactInformation} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
